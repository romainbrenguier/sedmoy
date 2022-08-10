package com.github.romainbrenguier.sedmoy.app;


import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.MissingPropertyException;
import groovy.lang.Script;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class GroovyInterpreter {

  private final GroovyShell shell = new GroovyShell();

  private final Binding binding;

  private Script script;

  public GroovyInterpreter() {
    binding = new Binding();
  }

  public void set(String variable, Object value) {
    shell.setProperty(variable, value);
  }

  public void setFromMap(Map<String, ?> map) {
    map.forEach(this::set);
  }

  public void setScript(String groovyScript) {
    script = shell.parse(groovyScript);
  }

  public Object run() throws GroovyException {
    try {
      return script.run();
    } catch (MissingPropertyException exception) {
      throw new GroovyException("Missing property in " + script,
          exception);
    } catch (Exception exception) {
      throw new GroovyException(script.toString(), exception);
    }
  }

  @Deprecated
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
