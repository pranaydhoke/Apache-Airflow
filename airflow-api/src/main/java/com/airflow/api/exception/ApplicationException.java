package com.airflow.api.exception;

public class ApplicationException extends RuntimeException {
    public ApplicationException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public ApplicationException(String message) {
        super(message);
    }
}
