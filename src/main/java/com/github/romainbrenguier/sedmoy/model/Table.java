package com.github.romainbrenguier.sedmoy.model;

import java.util.function.Function;

public interface Table {
  DataTable evaluate(Function<String, Table> environment);
}