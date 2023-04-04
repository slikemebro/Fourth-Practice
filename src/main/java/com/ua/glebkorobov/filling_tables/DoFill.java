package com.ua.glebkorobov.filling_tables;

import com.ua.glebkorobov.CreateConnectionWithDB;
import com.ua.glebkorobov.DoScripts;
import com.ua.glebkorobov.GetProperty;

import java.sql.Connection;

public class DoFill {

    public static void main(String[] args) {
        CreateConnectionWithDB connectionWithDB = new CreateConnectionWithDB();

        GetProperty property = new GetProperty("myProp.properties");
        Connection connection = connectionWithDB.getRemoteConnection(property);

        DoScripts doScripts = new DoScripts(connection);
        doScripts.runScript("DBCreateTables.sql");
        doScripts.runScript("DBFillLocation.sql");

        FillTypeTable fillTypeTable = new FillTypeTable();
        fillTypeTable.fill(connection, 500);

        FillGoodsTable fillGoodsTable = new FillGoodsTable();
        fillGoodsTable.fill(connection, 3000000);

        connectionWithDB.closeConnection(connection);
    }

}
