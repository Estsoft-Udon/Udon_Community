package com.example.estsoft_udon_community.email;

public class AuthCodeValidationException extends RuntimeException {
    public AuthCodeValidationException(String message) {
        super(message);
    }
}
