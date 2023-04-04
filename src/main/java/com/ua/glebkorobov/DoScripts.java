package com.ua.glebkorobov;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;

public class DoScripts {

    private static final Logger logger = LogManager.getLogger(DoScripts.class);

    private final Connection connection;

    public DoScripts(Connection connection) {
        this.connection = connection;
    }

    public void runScript(String fileName){
        try {
            ScriptRunner sr = new ScriptRunner(connection);
            Reader reader = new BufferedReader(new FileReader(fileName));
            sr.runScript(reader);
            logger.info("Scripts was done");
        } catch (FileNotFoundException e) {
            logger.warn(e.toString());
        }
    }

}
