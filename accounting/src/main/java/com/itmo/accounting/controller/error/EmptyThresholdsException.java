package com.itmo.accounting.controller.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class EmptyThresholdsException extends RuntimeException {
    public EmptyThresholdsException() {
        super();
    }

    public EmptyThresholdsException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public EmptyThresholdsException(final String message) {
        super(message);
    }

    public EmptyThresholdsException(final Throwable cause) {
        super(cause);
    }
}
