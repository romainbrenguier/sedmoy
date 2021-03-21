package com.github.romainbrenguier.sedmoy.operation;

public class PopOperation implements Operation {

  @Override
  public State apply(State s) {
    s.data = s.stack.pop();
    return s;
  }
}
