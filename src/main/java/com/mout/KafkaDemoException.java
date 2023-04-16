package com.mout;

public class KafkaDemoException extends RuntimeException {

    public KafkaDemoException(String message) {
        super(message);
    }

    public KafkaDemoException(String message, Throwable cause) {
        super(message, cause);
    }
}
