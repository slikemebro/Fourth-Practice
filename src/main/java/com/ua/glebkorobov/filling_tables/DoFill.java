package com.ua.glebkorobov.filling_tables;

import com.ua.glebkorobov.CreateConnectionWithDB;
import com.ua.glebkorobov.DoScripts;
import com.ua.glebkorobov.GetProperty;

import java.sql.Connection;

public class DoFill {

    private static final String NAME_PROP_FILE = "myProp.properties";

    public static void main(String[] args) {
        CreateConnectionWithDB connectionWithDB = new CreateConnectionWithDB();

        GetProperty property = new GetProperty(NAME_PROP_FILE);
        Connection connection = connectionWithDB.getRemoteConnection(property);

        DoScripts doScripts = new DoScripts(connection);
        doScripts.runScript("DBCreateTables.sql");
        doScripts.runScript("DBFillLocation.sql");

        FillTypeTable fillTypeTable = new FillTypeTable();
        fillTypeTable.fill(connection, fillTypeTable.createCSVReader());

        FillProductTable fillProductTable = new FillProductTable(property);
        fillProductTable.fill(connection);

        FillGoodsTable fillGoodsTable = new FillGoodsTable(property);
        fillGoodsTable.fastFill(connection);

        connectionWithDB.closeConnection(connection);
    }

}
