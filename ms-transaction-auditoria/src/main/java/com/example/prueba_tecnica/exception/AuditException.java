package com.example.prueba_tecnica.exception;

public class AuditException extends RuntimeException {
    private String errorMessage;

    public AuditException(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
