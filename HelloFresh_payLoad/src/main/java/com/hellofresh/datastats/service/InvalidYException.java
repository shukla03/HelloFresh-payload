package com.hellofresh.datastats.service;

public class InvalidYException extends Exception {
    private String code;

    public InvalidYException(String code, String message){
        super(message);
        this.setCode(code);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
