package com.example.mcs.exception;

public class ConversionException extends RuntimeException{
    public ConversionException(String message, Throwable cause) {
        super(message, cause);
    }
}
