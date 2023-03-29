package com.ua.glebkorobov;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {

    private static final Logger logger = LogManager.getLogger(Main.class);
    private static final GetProperty property = new GetProperty("myProp.properties");


    public static void main(String[] args) {
        getRemoteConnection();
    }

    private static Connection getRemoteConnection() {
        if (property.getValueFromProperty("rds_hostname") != null) {
            try {
                Class.forName("org.postgresql.Driver");
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
            catch (ClassNotFoundException e) { logger.warn(e.toString());}
            catch (SQLException e) { logger.warn(e.toString());}
        }
        return null;
    }

}