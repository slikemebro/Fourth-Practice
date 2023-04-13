package com.ua.glebkorobov.filling_tables;

import com.ua.glebkorobov.GetProperty;
import com.ua.glebkorobov.exceptions.FillingGoodsException;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class FillProductTableTest {


    @Test
    void fillTest() throws SQLException {
        GetProperty property = mock(GetProperty.class);
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);

        int type = 10;
        int countOfPropForProducts = 2;
        int maxProducts = 10;
        int sizeOfBatch = 100;
        int sizeOfMulti = 10;

        when(property.getValueFromProperty("count_of_types")).thenReturn(String.valueOf(type));
        when(property.getValueFromProperty("count_properties_of_products")).
                thenReturn(String.valueOf(countOfPropForProducts));
        when(property.getValueFromProperty("max_products")).thenReturn(String.valueOf(maxProducts));
        when(property.getValueFromProperty("size_of_batch")).thenReturn(String.valueOf(sizeOfBatch));
        when(property.getValueFromProperty("size_of_multi")).thenReturn(String.valueOf(sizeOfMulti));

        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeBatch()).thenReturn(new int[]{});

        FillProductTable fillProductTable = new FillProductTable(property);

        fillProductTable.fill(connection);

        verify(connection, times(1)).setAutoCommit(false);
        verify(connection, times(1)).prepareStatement(anyString());
        verify(connection, times(1)).commit();
        verify(statement, atLeastOnce()).setString(anyInt(), anyString());
        verify(statement, atLeastOnce()).setInt(anyInt(), anyInt());
        verify(statement, atLeastOnce()).addBatch();
        verify(statement, times(1)).executeBatch();
    }

    @Test
    void testFillExceptionExecuteBatch() throws SQLException {
        GetProperty property = mock(GetProperty.class);
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);

        int type = 10;
        int countOfPropForProducts = 2;
        int maxProducts = 10;
        int sizeOfBatch = 100;
        int sizeOfMulti = 10;

        when(property.getValueFromProperty("count_of_types")).thenReturn(String.valueOf(type));
        when(property.getValueFromProperty("count_properties_of_products")).
                thenReturn(String.valueOf(countOfPropForProducts));
        when(property.getValueFromProperty("max_products")).thenReturn(String.valueOf(maxProducts));
        when(property.getValueFromProperty("size_of_batch")).thenReturn(String.valueOf(sizeOfBatch));
        when(property.getValueFromProperty("size_of_multi")).thenReturn(String.valueOf(sizeOfMulti));

        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeBatch()).thenThrow(new SQLException());

        FillProductTable fillProductTable = new FillProductTable(property);

        assertThrows(FillingGoodsException.class, () -> fillProductTable.fill(connection));
    }
}