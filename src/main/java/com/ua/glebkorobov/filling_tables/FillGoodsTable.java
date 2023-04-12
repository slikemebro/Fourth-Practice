package com.ua.glebkorobov.filling_tables;

import com.ua.glebkorobov.GetProperty;
import com.ua.glebkorobov.dto.Product;
import com.ua.glebkorobov.exceptions.FillingGoodsException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FillGoodsTable {

    private static final Logger logger = LogManager.getLogger(FillGoodsTable.class);

    private final GetProperty property;

    private static final int BATCH_SIZE = 1000;


    public FillGoodsTable(GetProperty property) {
        this.property = property;
    }


    //100_000 per 88sec
    //rps 1250
    public void fill(Connection connection) {

        StopWatch watch = StopWatch.create();

        int addresses = Integer.parseInt(property.getValueFromProperty("count_of_addresses"));
        int products = Integer.parseInt(property.getValueFromProperty("count_of_products"));
        int quantity = Integer.parseInt(property.getValueFromProperty("max_quantity"));
        int maxSumOfQuantity = Integer.parseInt(property.getValueFromProperty("max_sum_of_quantity"));

        try {
            connection.setAutoCommit(false);
            PreparedStatement statement =
                    connection.prepareStatement(
                            "insert into goods (location_id, type_id, product_name, quantity) " +
                                    "values (?, ?, ?, ?);");

            Random random = new Random();

            AtomicInteger counter = new AtomicInteger();

            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            Validator validator = factory.getValidator();


            AtomicInteger counterForBatch = new AtomicInteger(0);

            watch.start();
            logger.info("time started");
            Stream.generate(() -> new Product(
                            random.nextInt(addresses) + 1,
                            random.nextInt(products) + 1,
                            random.nextInt(quantity) + 1,
                            RandomStringUtils.randomAlphabetic(5, 10).toLowerCase()))
                    .peek(p -> {
                        if (validator.validate(p).isEmpty()) {
                            counter.addAndGet(1);
                        } else {
                            p.setValid(false);
                            logger.debug("non valid product {}", p);
                        }
                    })
                    .takeWhile(p -> counter.intValue() < maxSumOfQuantity)
                    .filter(Product::isValid)
                    .limit(maxSumOfQuantity)
                    .forEach(p -> {
                        try {
                            counterForBatch.getAndIncrement();
                            statement.setInt(1, p.getAddress());
                            statement.setInt(2, p.getType());
                            statement.setString(3, p.getName());
                            statement.setInt(4, p.getQuantity());
                            statement.addBatch();
                            if (counterForBatch.intValue() % BATCH_SIZE == 0 ||
                                    counterForBatch.intValue() == maxSumOfQuantity) {
                                logger.info("Batch was execute {}", counterForBatch.intValue());
                                statement.executeBatch();
                            }
                        } catch (SQLException e) {
                            logger.warn(e.toString());
                            throw new FillingGoodsException(e);
                        }
                    });

            logger.info("Batch was execute");
            statement.executeBatch();
            logger.info("committing");
            connection.commit();
            logger.info("Fill goods time is = {} seconds", watch.getTime(TimeUnit.MILLISECONDS) * 0.001);
            watch.stop();
            statement.close();
        } catch (SQLException e) {
            logger.warn(e.toString());
            throw new FillingGoodsException(e);
        }
    }


    //1_000_000 products
    //1000 batch and multi rps 9090
    //100 batch and multi rps 7848
    //1000 batch and 100 multi rps 7011
    //100 batch and 1000 multi rps 9079
    //100 batch and 10000 multi rps
    //3_000_000 products
    //100 batch and 10000 multi rps 7662
    public void fastFill(Connection connection) {
        StopWatch watch = StopWatch.create();

        int addresses = Integer.parseInt(property.getValueFromProperty("count_of_addresses"));
        int products = Integer.parseInt(property.getValueFromProperty("count_of_products"));
        int quantity = Integer.parseInt(property.getValueFromProperty("max_quantity"));
        int maxSumOfQuantity = Integer.parseInt(property.getValueFromProperty("max_sum_of_quantity"));
        int countOfProp = Integer.parseInt(property.getValueFromProperty("count_properties_of_goods"));

        int sizeOfBatch = Integer.parseInt(property.getValueFromProperty("size_of_batch"));

        int sizeOfMulti = Integer.parseInt(property.getValueFromProperty("size_of_multi"));

        try {


            connection.setAutoCommit(false);


            StringBuilder sql =
                    new StringBuilder("insert into goods (location_id, type_id, product_name, quantity) values ");
            sql.append("(?, ?, ?, ?), ".repeat(sizeOfMulti - 1));
            sql.append("(?, ?, ?, ?);");


            PreparedStatement statement = connection.prepareStatement(sql.toString());

            Random random = new Random();
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            Validator validator = factory.getValidator();

            AtomicInteger counter = new AtomicInteger();

            List<Product> productList = Stream.generate(() -> new Product(
                            random.nextInt(addresses) + 1,
                            random.nextInt(products) + 1,
                            random.nextInt(quantity) + 1,
                            RandomStringUtils.randomAlphabetic(5, 10).toLowerCase()))
                    .peek(p -> {
                        if (validator.validate(p).isEmpty()) {
                            counter.addAndGet(1);
                        } else {
                            p.setValid(false);
                            logger.debug("non valid product {}", p);
                        }
                    })
                    .takeWhile(p -> counter.intValue() <= maxSumOfQuantity)
                    .filter(Product::isValid)
                    .collect(Collectors.toList());

            int counterForRequest = 1;
            int counterForBatch = 0;

            watch.start();
            logger.info("time started");

            for (Product p : productList) {
                statement.setInt(counterForRequest, p.getAddress());
                statement.setInt(counterForRequest + 1, p.getType());
                statement.setString(counterForRequest + 2, p.getName());
                statement.setInt(counterForRequest + 3, p.getQuantity());
                counterForRequest += countOfProp;

                if (counterForRequest >= sizeOfMulti * countOfProp) {
                    statement.addBatch();
                    counterForBatch++;
                    counterForRequest = 1;
                }

                if (counterForBatch >= sizeOfBatch) {
                    statement.executeBatch();
                    counterForBatch = 0;
                }
            }

            statement.executeBatch();
            logger.info("Committing");
            connection.commit();
            double time = watch.getTime(TimeUnit.MILLISECONDS) * 0.001;
            logger.info("Fill goods time is = {} seconds", time);
            logger.info("Sent {} products", maxSumOfQuantity);
            logger.info("rps is {}", maxSumOfQuantity / time);
            watch.stop();
        } catch (SQLException e) {
            logger.warn(e.toString());
            throw new FillingGoodsException(e);
        }
    }


}
