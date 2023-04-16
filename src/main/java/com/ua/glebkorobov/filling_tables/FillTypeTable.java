package com.ua.glebkorobov.filling_tables;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import com.ua.glebkorobov.exceptions.FileFindException;
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

public class FillTypeTable {

    private static final Logger logger = LogManager.getLogger(FillTypeTable.class);

    public void fill(Connection connection, CSVReader reader) {
        try {
            int counter = 0;
            connection.setAutoCommit(false);
            PreparedStatement statement =
                    connection.prepareStatement("insert into type (name) values(?);");

            List<String[]> types = reader.readAll();

            for (String[] type : types) {
                if (counter > 100) {
                    statement.executeBatch();
                    logger.info("batch was execute");
                    counter = 0;
                }
                statement.setString(1, type[0]);
                statement.addBatch();
                counter++;
            }


            StopWatch watch = StopWatch.createStarted();

            statement.executeBatch();
            connection.commit();

            logger.info("Fill types time is = {} seconds", watch.getTime(TimeUnit.MILLISECONDS) * 0.001);
            watch.stop();

            statement.close();
        } catch (SQLException e) {
            logger.warn("Sql Exception", e);
        } catch (FileNotFoundException e) {
            logger.warn("File not found exception", e);
            throw new FileFindException(e);
        } catch (IOException | CsvException e) {
            logger.warn(e.toString());
            throw new RuntimeException(e);
        }
    }

    public CSVReader createCSVReader() {
        try {
            return new CSVReaderBuilder(new FileReader("tables/types.csv")).build();
        } catch (FileNotFoundException e) {
            logger.warn(e.toString());
        }
        return null;
    }
}
