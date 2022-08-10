package com.github.romainbrenguier.sedmoy.app;

import java.util.Map;

public class Maps {

  public static <V> void renameKey(Map<String, V> map, String oldName,
      String newName) {
    if (oldName.equals(newName)) return;
    final V value = map.get(oldName);
    map.put(newName, value);
    map.remove(oldName);
  }
}

