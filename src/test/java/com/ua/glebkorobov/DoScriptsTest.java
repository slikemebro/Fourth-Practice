package com.ua.glebkorobov;

import com.ua.glebkorobov.exceptions.FileFindException;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

class DoScriptsTest {

    @Test
    void runScriptShouldCallScriptRunner() {
        Connection connection = mock(Connection.class);
        DoScripts doScripts = new DoScripts(connection);

        assertThrows(FileFindException.class, () -> doScripts.runScript(""));
    }
}