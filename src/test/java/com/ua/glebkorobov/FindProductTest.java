package com.ua.glebkorobov;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class FindProductTest {

    @Test
    void getResultTest() throws SQLException {
        ResultSet resultSet = mock(ResultSet.class);

        String address = "test";

        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getString("address")).thenReturn(address);

        FindProduct findProduct = new FindProduct();

        String expected = "Address is " + address;
        String actual = findProduct.getResult(resultSet);

        assertEquals(expected, actual);
        verify(resultSet, times(2)).next();
        verify(resultSet, times(1)).getString("address");
    }

    @Test
    void testGetResultReturnsNullOnException() throws SQLException {
        ResultSet resultSet = mock(ResultSet.class);
        FindProduct findProduct = new FindProduct();

        when(resultSet.next()).thenThrow(new SQLException("Database connection error"));

        assertNull(findProduct.getResult(resultSet));
        verify(resultSet, times(1)).next();
    }

    @Test
    void testFindSuccess() throws SQLException {
        Connection mockedConnection = mock(Connection.class);
        PreparedStatement mockedStatement = mock(PreparedStatement.class);
        ResultSet mockedResultSet = mock(ResultSet.class);

        when(mockedResultSet.next()).thenReturn(true, false);
        when(mockedResultSet.getString("address"))
                .thenReturn("Епіцентр Київ Кільцева дорога, 1-Б");

        when(mockedConnection.prepareStatement(any(String.class))).thenReturn(mockedStatement);
        when(mockedStatement.executeQuery()).thenReturn(mockedResultSet);

        String productName = "Test";
        FindProduct findProduct = new FindProduct();
        findProduct.find(mockedConnection, productName);

        verify(mockedStatement).setString(1, productName);
        verify(mockedStatement).executeQuery();
    }

    @Test
    void testFindEmpty() throws SQLException {
        Connection mockedConnection = mock(Connection.class);
        PreparedStatement mockedStatement = mock(PreparedStatement.class);
        ResultSet mockedResultSet = mock(ResultSet.class);

        when(mockedResultSet.next()).thenReturn(false);

        when(mockedConnection.prepareStatement(any(String.class))).thenReturn(mockedStatement);
        when(mockedStatement.executeQuery()).thenReturn(mockedResultSet);

        String productName = "Test";
        FindProduct findProduct = new FindProduct();
        findProduct.find(mockedConnection, productName);

        verify(mockedStatement).setString(1, productName);
        verify(mockedStatement).executeQuery();
    }

    @Test
    void testFindException() throws SQLException {
        Connection mockedConnection = mock(Connection.class);
        PreparedStatement mockedStatement = mock(PreparedStatement.class);

        when(mockedConnection.prepareStatement(any(String.class))).thenReturn(mockedStatement);
        when(mockedStatement.executeQuery()).thenThrow(new SQLException());

        String productName = "Test";
        FindProduct findProduct = new FindProduct();
        findProduct.find(mockedConnection, productName);

        verify(mockedStatement).setString(1, productName);
        verify(mockedStatement).executeQuery();
    }
}