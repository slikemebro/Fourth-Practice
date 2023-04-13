package com.ua.glebkorobov.filling_tables;

import com.ua.glebkorobov.GetProperty;
import com.ua.glebkorobov.exceptions.FillingGoodsException;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class FillGoodsTable {

    private static final Logger logger = LogManager.getLogger(FillGoodsTable.class);

    private final GetProperty property;

    public FillGoodsTable(GetProperty property) {
        this.property = property;
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
        int products = Integer.parseInt(property.getValueFromProperty("max_products"));
        int quantity = Integer.parseInt(property.getValueFromProperty("max_quantity"));
        int maxSumOfGoods = Integer.parseInt(property.getValueFromProperty("max_sum_of_goods"));
        int countOfProp = Integer.parseInt(property.getValueFromProperty("count_properties_of_goods"));

        int sizeOfBatch = Integer.parseInt(property.getValueFromProperty("size_of_batch"));

        int sizeOfMulti = Integer.parseInt(property.getValueFromProperty("size_of_multi"));

        try {


            connection.setAutoCommit(false);


            StringBuilder sql =
                    new StringBuilder("insert into goods (location_id, product_id, quantity) values ");
            sql.append("(?, ?, ?), ".repeat(sizeOfMulti - 1));
            sql.append("(?, ?, ?);");


            PreparedStatement statement = connection.prepareStatement(sql.toString());

            Random random = new Random();

            int counterForRequest = 1;
            int counterForBatch = 0;

            watch.start();
            logger.info("time started");

            for (int i = 0; i < maxSumOfGoods; i++) {
                statement.setInt(counterForRequest, random.nextInt(addresses) + 1);
                statement.setInt(counterForRequest + 1, random.nextInt(products) + 1);
                statement.setInt(counterForRequest + 2, random.nextInt(quantity) + 1);

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
            logger.info("Sent {} goods", maxSumOfGoods);
            logger.info("rps is {}", maxSumOfGoods / time);
            watch.stop();
        } catch (SQLException e) {
            logger.warn(e.toString());
            throw new FillingGoodsException(e);
        }
    }


}
