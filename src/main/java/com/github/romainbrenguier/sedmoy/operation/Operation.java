package com.github.romainbrenguier.sedmoy.operation;

import java.util.Stack;

public interface Operation {

  class State {
    Stack<Object> stack;
    Object data;
  }

  State apply(State s);
}
