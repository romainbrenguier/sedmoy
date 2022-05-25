package com.github.romainbrenguier.sedmoy.app;

import java.io.File;

public class GroovyException extends Exception {

    private final File groovyFile;

    GroovyException(File groovyFile, Exception cause) {
        super(cause);
        this.groovyFile = groovyFile;
    }

    @Override
    public String getMessage() {
        return "Error while reading script: " + groovyFile;
    }
}
