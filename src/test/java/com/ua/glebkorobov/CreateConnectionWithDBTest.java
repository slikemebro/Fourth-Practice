package com.ua.glebkorobov;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class CreateConnectionWithDBTest {

//    @Test
//    void testCloseConnection() throws SQLException {
//        CreateConnectionWithDB example = new CreateConnectionWithDB();
//        Connection mockConnection = mock(Connection.class);
//
//        example.closeConnection(mockConnection);
//
//        verify(mockConnection, times(1)).close();
//    }
//
//
//    @Test
//    void testGetRemoteConnectionMissingValues() {
//        GetProperty mockProperty = mock(GetProperty.class);
//        when(mockProperty.getValueFromProperty("rds_hostname")).thenReturn(null);
//
//        CreateConnectionWithDB connectionWithDB = new CreateConnectionWithDB();
//
//        Connection actual = connectionWithDB.getRemoteConnection(mockProperty);
//
//        assertNull(actual);
//        verify(mockProperty, times(1)).getValueFromProperty("rds_hostname");
//    }
}