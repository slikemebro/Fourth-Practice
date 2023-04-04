package com.ua.glebkorobov.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CreateDBConnectionExceptionTest {


    @Test
    void testCreateDBConnectionException() {
        CreateDBConnectionException exception = new CreateDBConnectionException();
        assertNull(exception.getMessage());
    }

    @Test
    void testCreateDBConnectionExceptionWithMessage() {
        String message = "Connection to the database could not be established";
        CreateDBConnectionException exception = new CreateDBConnectionException(message);
        assertEquals(message, exception.getMessage());
    }

    @Test
    void testCreateDBConnectionExceptionWithCause() {
        String message = "Connection to the database could not be established";
        Throwable cause = new Throwable("Database is not available");
        CreateDBConnectionException exception = new CreateDBConnectionException(message, cause);
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void testCreateDBConnectionExceptionWithCauseAndSuppression() {
        String message = "Connection to the database could not be established";
        Throwable cause = new Throwable("Database is not available");
        boolean enableSuppression = true;
        boolean writableStackTrace = true;
        CreateDBConnectionException exception = new CreateDBConnectionException(message, cause, enableSuppression, writableStackTrace);
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertTrue(exception.getStackTrace().length > 0);
    }

}