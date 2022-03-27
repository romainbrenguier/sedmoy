package com.github.romainbrenguier.sedmoy;


import groovy.lang.Binding;
import groovy.lang.GroovyShell;

public class GroovyInterpreter {

  public static void main(String[] args) {
    final Binding binding = new Binding();
    final GroovyShell shell = new GroovyShell(binding);
    shell.evaluate("println 'hello'");
  }
}
