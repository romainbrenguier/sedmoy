package com.github.romainbrenguier.sedmoy.operation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/** Represents a method with parameters */
public class MethodOperation implements Operation {
  final Method method;
  final List<Parameter> parameters;

  public MethodOperation(Method method, List<Parameter> parameters) {
    this.method = method;
    this.parameters = parameters;
  }

  @Override
  public State apply(State state) {
    try {
      Object[] actualParameters = parameters.stream()
          .map(parameter -> parameter.eval(state))
          .toArray();
      state.data = method.invoke(state.data, actualParameters);
    } catch (IllegalAccessException | InvocationTargetException e) {
      state.data = e;
    }
    return state;
  }

  public Class<?> getReturnType() {
    return method.getReturnType();
  }
}
