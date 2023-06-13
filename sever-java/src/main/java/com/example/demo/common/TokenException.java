package com.example.demo.common;

public class TokenException extends RuntimeException{
    public TokenException() {super();}

    public TokenException(String msg) {
        super(msg);
    }
}