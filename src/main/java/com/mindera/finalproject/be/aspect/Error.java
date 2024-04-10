package com.mindera.finalproject.be.aspect;

import java.util.Date;

public class Error {

    private String message;
    private int status;
    private Date timestamp;

    private Error(Builder builder) {
        this.message = builder.message;
        this.status = builder.status;
        this.timestamp = builder.timestamp;
    }

    public Error() {
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public static class Builder {
        private String message;
        private int status;
        private Date timestamp;

        public Builder() {
        }


        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder status(int status) {
            this.status = status;
            return this;
        }

        public Builder timestamp(Date timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Error build() {
            return new Error(this);
        }
    }
}
