package com.ua.glebkorobov.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FileFindExceptionTest {

    @Test
    void testEmptyConstructor() {
        FileFindException exception = new FileFindException();
        assertNull(exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testMessageConstructor() {
        String message = "File not found";
        FileFindException exception = new FileFindException(message);
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testCauseConstructor() {
        Throwable cause = new RuntimeException("File not found");
        FileFindException exception = new FileFindException(cause);
        assertEquals(cause, exception.getCause());
        assertEquals(cause.toString(), exception.getMessage());
    }

    @Test
    void testMessageAndCauseConstructor() {
        String message = "Error finding file";
        Throwable cause = new RuntimeException("File not found");
        FileFindException exception = new FileFindException(message, cause);
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void testMessageCauseEnableSuppressionWritableStackTraceConstructor() {
        String message = "Error finding file";
        Throwable cause = new RuntimeException("File not found");
        boolean enableSuppression = true;
        boolean writableStackTrace = true;
        FileFindException exception = new FileFindException(message, cause, enableSuppression, writableStackTrace);
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertTrue(exception.getStackTrace().length > 0);
    }

}