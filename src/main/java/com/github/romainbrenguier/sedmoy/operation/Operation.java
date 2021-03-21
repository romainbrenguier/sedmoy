package com.github.romainbrenguier.sedmoy.operation;

import java.util.Stack;

public interface Operation {

  class State {
    public Stack<Object> stack;
    public Object data;
    public State(Stack<Object> stack, Object data) {
      this.stack = stack;
      this.data = data;
    }
  }

  State apply(State s);
}
