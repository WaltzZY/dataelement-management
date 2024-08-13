package com.inspur.dsp.direct.exception;

/**
 * 自定义异常
 */
public class CustomException extends RuntimeException {
    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public CustomException(String message) {
        super(message);
    }
}
