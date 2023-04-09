package com.ua.glebkorobov;

import com.ua.glebkorobov.exceptions.CreateDBConnectionException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class CreateConnectionWithDB {

    private static final Logger logger = LogManager.getLogger(CreateConnectionWithDB.class);


    public Connection getRemoteConnection(GetProperty property) {
        if (property.getValueFromProperty("rds_hostname") != null) {
            try {
                String dbName = property.getValueFromProperty("rds_db_name");
                String userName = property.getValueFromProperty("rds_username");
                String password = property.getValueFromProperty("rds_password");
                String hostname = property.getValueFromProperty("rds_hostname");
                String port = property.getValueFromProperty("rds_port");
                String jdbcUrl = "jdbc:postgresql://" + hostname + ":" + port + "/" + dbName + "?user=" + userName + "&password=" + password;
                logger.trace("Getting remote connection with connection string from environment variables.");
                Connection con = DriverManager.getConnection(jdbcUrl);
                logger.info("Remote connection successful.");
                return con;
            }
            catch (SQLException e) {
                logger.warn(e.toString());
                throw new CreateDBConnectionException(e);
            }
        }
        return null;
    }

    public void closeConnection(Connection connection){
        try {
            connection.close();
        } catch (SQLException e) {
            logger.warn(e.toString());
            throw new CreateDBConnectionException(e);
        }
    }
}
