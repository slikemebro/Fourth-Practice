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

public class FillProductTable {


    private static final Logger logger = LogManager.getLogger(FillProductTable.class);

    private final GetProperty property;


    public FillProductTable(GetProperty property) {
        this.property = property;
    }

    public void fill(Connection connection) {
        StopWatch watch = StopWatch.create();

        int type = Integer.parseInt(property.getValueFromProperty("count_of_types"));
        int countOfPropForProducts = Integer.parseInt(property.getValueFromProperty("count_properties_of_products"));
        int maxProducts = Integer.parseInt(property.getValueFromProperty("max_products"));

        int sizeOfBatch = Integer.parseInt(property.getValueFromProperty("size_of_batch"));

        int sizeOfMulti = Integer.parseInt(property.getValueFromProperty("size_of_multi"));

        try {
            connection.setAutoCommit(false);

            StringBuilder sql =
                    new StringBuilder("insert into product (name, type_id) values ");
            sql.append("(?, ?), ".repeat(sizeOfMulti - 1));
            sql.append("(?, ?);");


            PreparedStatement statement = connection.prepareStatement(sql.toString());

            Random random = new Random();
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            Validator validator = factory.getValidator();

            AtomicInteger counter = new AtomicInteger();

            List<Product> productList = Stream.generate(() -> new Product(
                    RandomStringUtils.randomAlphabetic(5, 10).toLowerCase(),
                    random.nextInt(type) + 1))
                    .peek(p -> {
                        if (validator.validate(p).isEmpty()) {
                            counter.addAndGet(1);
                        } else {
                            p.setValid(false);
                            logger.debug("non valid product {}", p);
                        }
                    })
                    .takeWhile(p -> counter.intValue() <= maxProducts)
                    .filter(Product::isValid)
                    .collect(Collectors.toList());

            int counterForRequest = 1;
            int counterForBatch = 0;

            watch.start();
            logger.info("time started");

            for (Product p : productList) {
                statement.setString(counterForRequest, p.getName());
                statement.setInt(counterForRequest + 1, p.getType());

                counterForRequest += countOfPropForProducts;

                if (counterForRequest >= sizeOfMulti * countOfPropForProducts) {
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
            logger.info("Fill product time is = {} seconds", time);
            logger.info("Sent {} products", maxProducts);
            logger.info("rps is {}", maxProducts / time);
            watch.stop();
        } catch (SQLException e) {
            logger.warn("Sql exception", e);
            throw new FillingGoodsException(e);
        }
    }
}
