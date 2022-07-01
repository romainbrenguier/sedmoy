package com.github.romainbrenguier.sedmoy.app;


import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.MissingPropertyException;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class GroovyInterpreter {

  private final Binding binding;

  public GroovyInterpreter() {
    binding = new Binding();
  }

  public void set(String variable, Object value) {
    binding.setProperty(variable, value);
  }

  public void setFromMap(Map<String, ?> map) {
    map.forEach(this::set);
  }

  public Object run(String groovyScript) throws GroovyException {
    try {
      return new GroovyShell(binding).evaluate(groovyScript);
    } catch (MissingPropertyException exception) {
      throw new GroovyException("Missing property in " + groovyScript,
          exception);
    } catch (Exception exception) {
      throw new GroovyException(groovyScript, exception);
    }
  }

  public void run(File groovyScript) throws GroovyException {
    final GroovyShell shell = new GroovyShell(binding);
    System.out.println("=== Sedmoy ===");
    System.out.println(
        "Welcome to Sedmoy's groovy interpreter. The following variables are accessible:\n");
    binding.getVariables().forEach((key, value) ->
            System.out.println(key + " of type " + value.getClass() + "\n"));
    try {
      final Object result = shell.evaluate(groovyScript);
      if (result != null) {
        System.out.println(result);
      }
    } catch (IOException exception) {
      throw new GroovyException(groovyScript.getAbsolutePath(), exception);
    }
  }

}
