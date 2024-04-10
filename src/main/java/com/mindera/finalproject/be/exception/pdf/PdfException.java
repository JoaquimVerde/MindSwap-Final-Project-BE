package com.mindera.finalproject.be.exception.pdf;

import java.io.IOException;

public class PdfException extends IOException {
    public PdfException(String message) {
        super(message);
    }
}
