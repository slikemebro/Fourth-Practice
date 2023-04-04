package com.ua.glebkorobov;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

public class CreatingTables {

    private static final Logger logger = LogManager.getLogger(CreatingTables.class);

    public void dropTables(Connection connection){
        try {
            StopWatch watch = StopWatch.createStarted();
            PreparedStatement statement =
                    connection.prepareStatement("");

            statement.executeQuery();


            logger.info("Find time is = {} seconds", watch.getTime(TimeUnit.MILLISECONDS) * 0.001);
            watch.stop();


        } catch (SQLException e) {
            logger.warn(e.toString());
        }
    }
}
