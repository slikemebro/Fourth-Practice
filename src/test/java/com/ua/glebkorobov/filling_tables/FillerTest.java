package com.ua.glebkorobov.filling_tables;

import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.mockito.Mockito.*;

class FillerTest {

    @Test
    void testFill() {
        Connection mockConnection = mock(Connection.class);
        Filler filler = mock(Filler.class);

        filler.fill(mockConnection);

        verify(filler, times(1)).fill(mockConnection);
    }

}