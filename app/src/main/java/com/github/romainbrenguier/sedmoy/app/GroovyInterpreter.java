package com.github.romainbrenguier.sedmoy.app;


import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyShell;
import groovy.lang.MissingPropertyException;
import groovy.lang.Script;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.Map;
import org.codehaus.groovy.control.CompilationFailedException;

public class GroovyInterpreter {

  private final GroovyShell shell;

  private final Binding binding;

  private Script script;

  public GroovyInterpreter() {
    binding = new Binding();
    shell = new GroovyShell();
  }

  public GroovyInterpreter(ClassLoader classLoader) {
    binding = new Binding();
    shell = new GroovyShell(new GroovyClassLoader(classLoader));
  }

  public GroovyInterpreter(Path directory) throws MalformedURLException {
    this(new URLClassLoader(
        new URL[]{directory.toUri().toURL()},
        GroovyInterpreter.class.getClassLoader()));
  }

  public void set(String variable, Object value) {
    shell.setProperty(variable, value);
  }

  public void setFromMap(Map<String, ?> map) {
    map.forEach(this::set);
  }

  public void setScript(String groovyScript) throws GroovyException {
    try {
      script = shell.parse(groovyScript);
    } catch (CompilationFailedException e) {
      throw new GroovyException("Compilation failed: " + groovyScript, e);
    }
  }

  public void setCachedFileReader(CachedFileReader cachedFileReader) {
    set("cachedFileReader", cachedFileReader);
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
