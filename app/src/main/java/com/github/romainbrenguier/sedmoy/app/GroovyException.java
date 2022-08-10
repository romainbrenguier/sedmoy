package com.github.romainbrenguier.sedmoy.app;

import java.io.File;

public class GroovyException extends Exception {

    private final String source;

    GroovyException(String source, Exception cause) {
        super(cause);
        this.source = source;
    }

    @Override
    public String getMessage() {
        return "Error while reading script: " + source;
    }
}
