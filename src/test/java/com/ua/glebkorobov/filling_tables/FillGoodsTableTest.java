package com.ua.glebkorobov.filling_tables;

import com.ua.glebkorobov.GetProperty;
import com.ua.glebkorobov.exceptions.FillingGoodsException;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class FillGoodsTableTest {

    @Test
    void fillTest() throws SQLException {
        GetProperty property = mock(GetProperty.class);
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);

        int addresses = 5;
        int products = 10;
        int quantity = 20;
        int maxSumOfQuantity = 3;
        int countOfProp = 4;
        int sizeOfBatch = 100;
        int sizeOfMulti = 3;

        when(property.getValueFromProperty("count_of_addresses")).thenReturn(String.valueOf(addresses));
        when(property.getValueFromProperty("count_of_products")).thenReturn(String.valueOf(products));
        when(property.getValueFromProperty("max_quantity")).thenReturn(String.valueOf(quantity));
        when(property.getValueFromProperty("max_sum_of_quantity")).thenReturn(String.valueOf(maxSumOfQuantity));
        when(property.getValueFromProperty("count_properties_of_goods")).thenReturn(String.valueOf(countOfProp));
        when(property.getValueFromProperty("size_of_batch")).thenReturn(String.valueOf(sizeOfBatch));
        when(property.getValueFromProperty("size_of_multi")).thenReturn(String.valueOf(sizeOfMulti));
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeBatch()).thenReturn(new int[]{});


        FillGoodsTable fillGoodsTable = new FillGoodsTable(property);

        fillGoodsTable.fastFill(connection);

        verify(connection, times(1)).setAutoCommit(false);
        verify(connection, times(1)).prepareStatement(anyString());
        verify(connection, times(1)).commit();
        verify(statement, atLeastOnce()).setInt(anyInt(), anyInt());
        verify(statement, atLeastOnce()).addBatch();
        verify(statement, times(1)).executeBatch();
    }

    @Test
    void testFillExceptionExecuteBatch() throws SQLException {
        GetProperty property = mock(GetProperty.class);
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);

        int addresses = 5;
        int products = 10;
        int quantity = 20;
        int maxSumOfQuantity = 100;
        int countOfProp = 4;
        int sizeOfBatch = 100;
        int sizeOfMulti = 100;

        when(property.getValueFromProperty("count_of_addresses")).thenReturn(String.valueOf(addresses));
        when(property.getValueFromProperty("count_of_products")).thenReturn(String.valueOf(products));
        when(property.getValueFromProperty("max_quantity")).thenReturn(String.valueOf(quantity));
        when(property.getValueFromProperty("max_sum_of_quantity")).thenReturn(String.valueOf(maxSumOfQuantity));
        when(property.getValueFromProperty("count_properties_of_goods")).thenReturn(String.valueOf(countOfProp));
        when(property.getValueFromProperty("size_of_batch")).thenReturn(String.valueOf(sizeOfMulti));
        when(property.getValueFromProperty("size_of_multi")).thenReturn(String.valueOf(sizeOfBatch));
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeBatch()).thenThrow(new SQLException());

        FillGoodsTable fillGoodsTable = new FillGoodsTable(property);

        assertThrows(FillingGoodsException.class, () -> fillGoodsTable.fastFill(connection));
    }
}