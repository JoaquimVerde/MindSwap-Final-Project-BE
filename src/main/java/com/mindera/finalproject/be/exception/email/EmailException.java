package com.mindera.finalproject.be.exception.email;

import java.io.IOException;

public class EmailException extends IOException {
    public EmailException(String message) {
        super(message);
    }
}
