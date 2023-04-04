package com.ua.glebkorobov.filling_tables;

import com.ua.glebkorobov.dto.Product;
import com.ua.glebkorobov.dto.ValidateDto;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class FillGoodsTable implements Filler {

    private static final Logger logger = LogManager.getLogger(FillGoodsTable.class);

    @Override
    public void fill(Connection connection, int count) {
        try {
            connection.setAutoCommit(true);
            PreparedStatement statement =
                    connection.prepareStatement("insert into goods (address, name, quantity)\n" +
                            "values (?, ?, ?)\n" +
                            "on conflict (address, name)\n" +
                            "    do update set quantity = goods.quantity + excluded.quantity;");

            Random random = new Random();

            AtomicInteger counter = new AtomicInteger();

            ValidateDto validateDto = new ValidateDto();

            Stream.generate(() -> new Product(random.nextInt(11), random.nextInt(502),
                            random.nextInt(302)))
                    .peek(p -> {
                        if (validateDto.getValidate(p).isEmpty()) {
                            counter.addAndGet(p.getQuantity());
                        } else {
                            p.setValid(false);
                            logger.debug("non valid product {}", p);
                        }
                    })
                    .takeWhile(p -> counter.intValue() < 3000000)
                    .filter(p -> p.isValid())
                    .forEach(p -> {
                        try {
                            statement.setInt(1, p.getAddress());
                            statement.setInt(2, p.getName());
                            statement.setInt(3, p.getQuantity());
                            statement.addBatch();
                        } catch (SQLException e) {
                            logger.warn(e.toString());
                        }
                    });

            logger.info(counter.intValue());

            StopWatch watch = StopWatch.createStarted();
            statement.executeBatch();
            logger.info("Fill goods time is = {} seconds", watch.getTime(TimeUnit.MILLISECONDS) * 0.001);
            watch.stop();
            statement.close();
        } catch (SQLException e) {
            logger.warn(e.toString());
        }
    }

}