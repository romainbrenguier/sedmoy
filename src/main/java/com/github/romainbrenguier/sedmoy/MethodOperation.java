package com.github.romainbrenguier.sedmoy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/** Represents a method with parameters */
public class MethodOperation implements Operation {
  final Method method;
  final Object[] parameters;

  public MethodOperation(Method method, Object[] parameters) {
    this.method = method;
    this.parameters = parameters;
  }

  @Override
  public State apply(State state) {
    try {
      state.data = method.invoke(state.data, parameters);
    } catch (IllegalAccessException | InvocationTargetException e) {
      state.data = e;
    }
    return state;
  }

  public Class<?> getReturnType() {
    return method.getReturnType();
  }
}
