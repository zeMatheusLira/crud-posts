package com.example.crudposts.exceptions.custom;

public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}