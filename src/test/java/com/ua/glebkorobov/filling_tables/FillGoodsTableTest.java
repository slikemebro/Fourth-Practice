package com.ua.glebkorobov.filling_tables;

import com.ua.glebkorobov.GetProperty;
import com.ua.glebkorobov.dto.ValidateDto;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class FillGoodsTableTest {

//    @Test
//    void fillTest() throws SQLException {
//        GetProperty property = mock(GetProperty.class);
//        ValidateDto validateDto = mock(ValidateDto.class);
//        Connection connection = mock(Connection.class);
//        PreparedStatement statement = mock(PreparedStatement.class);
//
//        int addresses = 5;
//        int products = 10;
//        int quantity = 20;
//        int maxSumOfQuantity = 100;
//
//        when(property.getValueFromProperty("count_of_addresses")).thenReturn(String.valueOf(addresses));
//        when(property.getValueFromProperty("count_of_products")).thenReturn(String.valueOf(products));
//        when(property.getValueFromProperty("max_quantity")).thenReturn(String.valueOf(quantity));
//        when(property.getValueFromProperty("max_sum_of_quantity")).thenReturn(String.valueOf(maxSumOfQuantity));
//        when(connection.prepareStatement(anyString())).thenReturn(statement);
//        when(statement.executeBatch()).thenReturn(new int[]{});
//
//        FillGoodsTable fillGoodsTable = new FillGoodsTable(property, validateDto);
//
//        fillGoodsTable.fill(connection);
//
//        verify(connection, times(1)).setAutoCommit(true);
//        verify(connection, times(1)).prepareStatement(anyString());
//        verify(statement, atLeastOnce()).setInt(anyInt(), anyInt());
//        verify(statement, atLeastOnce()).addBatch();
//        verify(statement, times(1)).executeBatch();
//    }

}