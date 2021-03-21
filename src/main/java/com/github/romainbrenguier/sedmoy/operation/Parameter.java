package com.github.romainbrenguier.sedmoy.operation;

public interface Parameter {

  /**
   * Value of the parameter in the current state.
   */
  Object eval(Operation.State state);
}
