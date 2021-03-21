package com.github.romainbrenguier.sedmoy.operation;

public class PushOperation implements Operation {

  @Override
  public State apply(State s) {
    s.stack.push(s.data);
    return s;
  }
}
