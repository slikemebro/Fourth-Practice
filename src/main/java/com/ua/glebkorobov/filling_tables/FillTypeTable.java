package com.ua.glebkorobov.filling_tables;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class FillTypeTable implements Filler {

    private static final Logger logger = LogManager.getLogger(FillTypeTable.class);

    @Override
    public void fill(Connection connection, int count) {
        try {
            CSVReader reader = new CSVReaderBuilder(new FileReader("tables/types.csv")).build();

            connection.setAutoCommit(true);
            PreparedStatement statement =
                    connection.prepareStatement("INSERT INTO type (name)\n" +
                            "SELECT ?\n" +
                            "WHERE NOT EXISTS\n" +
                            "    (SELECT * FROM type WHERE name = ?);");

            List<String[]> types = reader.readAll();

            for (String[] type : types) {
                statement.setString(1, type[0]);
                statement.setString(2, type[0]);
                statement.addBatch();
            }

            StopWatch watch = StopWatch.createStarted();

            statement.executeBatch();
            logger.info("Fill types time is = {} seconds", watch.getTime(TimeUnit.MILLISECONDS) * 0.001);
            watch.stop();
            statement.close();
        } catch (SQLException e) {
            logger.warn(e.toString());
        } catch (FileNotFoundException e) {
            logger.warn(e.toString());
            throw new RuntimeException();
        } catch (IOException | CsvException e) {
            logger.warn(e.toString());
            throw new RuntimeException(e);
        }
    }
}
