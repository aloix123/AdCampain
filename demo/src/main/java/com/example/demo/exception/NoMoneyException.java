package com.example.demo.exception;

public class NoMoneyException extends RuntimeException {
    public NoMoneyException(String message) {
        super("you dont have much money to create this campain ");
    }
}
