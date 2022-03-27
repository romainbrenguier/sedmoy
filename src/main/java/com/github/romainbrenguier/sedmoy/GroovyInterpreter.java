package com.github.romainbrenguier.sedmoy;


import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import java.io.File;
import java.io.IOException;

public class GroovyInterpreter {

  public static void run(File groovyScript, Object input) throws GroovyException {
    final Binding binding = new Binding();
    final GroovyShell shell = new GroovyShell(binding);
    shell.setVariable("input", input);
    System.out.println("=== Sedmoy ===");
    System.out.println(
        "Welcome to Sedmoy's groovy interpreter. The input file is accessible from your Groovy\n"
            + "script through the 'input' variable.");
    try {
      final Object result = shell.evaluate(groovyScript);
      if (result != null) {
        System.out.println(result);
      }
    } catch (IOException exception) {
      throw new GroovyException(groovyScript, exception);
    }
  }

  static class GroovyException extends Exception {

    final private File groovyFile;

    GroovyException(File groovyFile, Exception cause) {
      super(cause);
      this.groovyFile = groovyFile;
    }

    @Override
    public String getMessage() {
      return "Error while reading script: " + groovyFile;
    }
  }
}
