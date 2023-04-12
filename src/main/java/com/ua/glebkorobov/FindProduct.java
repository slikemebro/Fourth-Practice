package com.ua.glebkorobov;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

public class FindProduct {

    private static final Logger logger = LogManager.getLogger(FindProduct.class);

    public ResultSet find(Connection connection, String productName) {
        StopWatch watch = StopWatch.createStarted();

        try {
            PreparedStatement statement =
                    connection.prepareStatement("select l.address, sum(quantity) as sum_of_quantity\n" +
                            "from goods g,\n" +
                            "     location l,\n" +
                            "     type t\n" +
                            "where l.id = g.location_id\n" +
                            "  and t.id = g.type_id\n" +
                            "  and t.name = ?\n" +
                            "group by l.address\n" +
                            "order by sum_of_quantity desc\n" +
                            "limit 1;");
            statement.setString(1, productName);
            ResultSet resultSet = statement.executeQuery();


            logger.info("Find time is = {} seconds", watch.getTime(TimeUnit.MILLISECONDS) * 0.001);
            watch.stop();

            return resultSet;
        } catch (SQLException e) {
            logger.warn(e.toString());
            return null;
        }
    }

    public String getResult(ResultSet resultSet) {
        try {
            String result = null;
            while (resultSet.next()) {
                 result = "Address is " + resultSet.getString("address");
            }
            resultSet.close();
            return result;
        } catch (SQLException e) {
            logger.warn(e.toString());
            return null;
        }
    }
}
