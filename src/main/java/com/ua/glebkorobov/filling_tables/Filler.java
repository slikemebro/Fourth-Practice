package com.ua.glebkorobov.filling_tables;

import java.sql.Connection;

public interface Filler {
    void fill(Connection connection, int count);
}
