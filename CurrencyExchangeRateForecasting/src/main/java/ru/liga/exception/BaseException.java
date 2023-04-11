package ru.liga.exception;

public abstract class BaseException extends RuntimeException {
    public BaseException(String s) {
        super(s);
    }
}
