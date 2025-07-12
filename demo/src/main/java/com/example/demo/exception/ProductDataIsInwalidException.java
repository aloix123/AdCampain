package com.example.demo.exception;

public class ProductDataIsInwalidException extends RuntimeException {
    public ProductDataIsInwalidException() {
        super("product data provided is invalid and do not siuts product from database, make sure to provide proper data");
    }
}
