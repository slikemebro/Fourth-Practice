package com.ua.glebkorobov.filling_tables;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.ua.glebkorobov.exceptions.FileFindException;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


class FillTypeTableTest {

    @Test
    void testFill() throws SQLException, IOException, CsvException {
        String[] type1 = new String[]{"type1"};
        String[] type2 = new String[]{"type2"};

        List<String[]> types = Arrays.asList(type1, type2);
        Connection connectionMock = mock(Connection.class);
        CSVReader csvReaderMock = mock(CSVReader.class);
        PreparedStatement statementMock = mock(PreparedStatement.class);

        FillTypeTable fillTypesTable = new FillTypeTable();

        when(connectionMock.prepareStatement(anyString())).thenReturn(statementMock);
        when(csvReaderMock.readAll()).thenReturn(types);

        fillTypesTable.fill(connectionMock, csvReaderMock);

        verify(statementMock, times(2)).setString(eq(1), anyString());
        verify(statementMock, times(2)).addBatch();
        verify(statementMock, times(1)).executeBatch();
        verify(statementMock, times(1)).close();
        verify(connectionMock, times(1)).commit();
        verify(connectionMock, times(1)).setAutoCommit(false);
        verify(csvReaderMock, times(1)).readAll();
    }

    @Test
    void testFillWhenFileNotFoundException() throws SQLException, IOException, CsvException {
        Connection connectionMock = mock(Connection.class);
        CSVReader csvReaderMock = mock(CSVReader.class);
        PreparedStatement statementMock = mock(PreparedStatement.class);

        FillTypeTable fillTypesTable = new FillTypeTable();

        when(connectionMock.prepareStatement(anyString())).thenReturn(statementMock);
        when(csvReaderMock.readAll()).thenThrow(new FileNotFoundException());

        assertThrows(FileFindException.class, () -> fillTypesTable.fill(connectionMock, csvReaderMock));
    }

    @Test
    void testCreateCSVReader() {
        FillTypeTable fillTypeTable = new FillTypeTable();

        assertNotNull(fillTypeTable.createCSVReader());
    }
}