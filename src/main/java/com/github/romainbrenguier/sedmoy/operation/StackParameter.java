package com.github.romainbrenguier.sedmoy.operation;

import com.github.romainbrenguier.sedmoy.operation.Operation.State;

public class StackParameter implements Parameter {

  /**
   * Starts from the top at 0
   */
  private final int indexOnStack;

  public StackParameter(int indexOnStack) {
    this.indexOnStack = indexOnStack;
  }

  @Override
  public Object eval(State state) {
    return state.stack.elementAt(state.stack.size() - indexOnStack - 1);
  }
}
