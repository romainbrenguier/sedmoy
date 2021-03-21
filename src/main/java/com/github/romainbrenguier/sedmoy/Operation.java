package com.github.romainbrenguier.sedmoy;

import java.util.Stack;

public interface Operation {

  Object apply(Stack<Object> stack, Object input);

}
