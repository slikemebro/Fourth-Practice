package com.ua.glebkorobov;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.util.Scanner;

public class Main {

    private static final Logger logger = LogManager.getLogger(Main.class);

    private static final String PRODUCT_NAME = "name";

    private static final GetProperty property = new GetProperty("myProp.properties");


    public static void main(String[] args) {
        CreateConnectionWithDB connectionWithDB = new CreateConnectionWithDB();

        Connection connection = connectionWithDB.getRemoteConnection(property);
        FindProduct findProduct = new FindProduct();

        String productName = System.getProperty(PRODUCT_NAME);
        logger.info("Get system property");

        if (productName == null) {
            logger.info("Got property was null. Enter by console");
            Scanner sc = new Scanner(System.in);
            logger.info("Enter name");
            productName = sc.next();
        }

        String result = findProduct.getResult(findProduct.find(connection, productName));

        logger.info("{}", result);

        connectionWithDB.closeConnection(connection);
    }
}