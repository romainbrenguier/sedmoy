package com.github.romainbrenguier.sedmoy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/** Represents a method with parameters */
public class Operation {
  final Method method;
  final Object[] parameters;

  public Operation(Method method, Object[] parameters) {
    this.method = method;
    this.parameters = parameters;
  }

  public Object apply(Object input) {
    try {
      return method.invoke(input, parameters);
    } catch (IllegalAccessException | InvocationTargetException e) {
      return e;
    }
  }

  public Class<?> getReturnType() {
    return method.getReturnType();
  }
}
