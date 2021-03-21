package com.github.romainbrenguier.sedmoy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Stack;

/** Represents a method with parameters */
public class MethodOperation implements Operation {
  final Method method;
  final Object[] parameters;

  public MethodOperation(Method method, Object[] parameters) {
    this.method = method;
    this.parameters = parameters;
  }

  @Override
  public Object apply(Stack<Object> stack, Object input) {
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
