package com.github.romainbrenguier.sedmoy.app;


import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GroovyInterpreter {
  Map<String, Object> variables = new HashMap<>();

  public void set(String variable, Object value) {
    variables.put(variable, value);
  }

  public void run(File groovyScript) throws GroovyException {
    final Binding binding = new Binding();
    final GroovyShell shell = new GroovyShell(binding);
    System.out.println("=== Sedmoy ===");
    System.out.println(
        "Welcome to Sedmoy's groovy interpreter. The following variables are accessible:\n");
    variables.entrySet().forEach(entry ->
            System.out.println(entry.getKey() + " of type " + entry.getValue().getClass() + "\n"));
    try {
      final Object result = shell.evaluate(groovyScript);
      if (result != null) {
        System.out.println(result);
      }
    } catch (IOException exception) {
      throw new GroovyException(groovyScript, exception);
    }
  }

}
