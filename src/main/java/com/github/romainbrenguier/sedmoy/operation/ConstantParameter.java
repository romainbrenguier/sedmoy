package com.github.romainbrenguier.sedmoy.operation;

import com.github.romainbrenguier.sedmoy.operation.Operation.State;

public class ConstantParameter implements Parameter {
  private final Object value;

  public ConstantParameter(Object value) {
    this.value = value;
  }

  @Override
  public Object eval(State state) {
    return value;
  }
}
