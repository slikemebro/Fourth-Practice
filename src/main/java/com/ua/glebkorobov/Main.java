package com.ua.glebkorobov;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.util.Scanner;

public class Main {

    private static final Logger logger = LogManager.getLogger(Main.class);


    public static void main(String[] args) {
        CreateConnectionWithDB connectionWithDB = new CreateConnectionWithDB();
        GetProperty property = new GetProperty("myProp.properties");
        Connection connection = connectionWithDB.getRemoteConnection(property);
        FindProduct findProduct = new FindProduct();


        Scanner sc = new Scanner(System.in);
        logger.info("Enter name");
        String productName = sc.next();

        String result = findProduct.getResult(findProduct.find(connection, productName));

        logger.info("{}", result);

        connectionWithDB.closeConnection(connection);
    }
}