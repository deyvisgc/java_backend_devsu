package com.example.prueba_tecnica.exception;

public class AccountException extends RuntimeException {
    private String errorMessage;

    public AccountException(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
