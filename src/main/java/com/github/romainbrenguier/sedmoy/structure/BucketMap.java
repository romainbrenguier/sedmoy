package com.github.romainbrenguier.sedmoy.structure;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

/** Map indices to buckets */
public class BucketMap<T> {
  final int bucketSize = 90;
  final int granularity = 10;

  Comparator<T> comparator;

  Map<Integer, Set<T>> map = new HashMap<>();

  private int size = 0;

  public BucketMap(Comparator<T> comparator) {
    this.comparator = comparator;
  }

  public boolean put(int index, T data) {
    int mapIndex = (index / granularity) * granularity;
    Set<T> previous = map.computeIfAbsent(mapIndex, i -> new TreeSet<T>(comparator));
    if (previous.size() >= bucketSize) {
      return false;
    }
    /*
    if (previous.size() >= granularity) {
      System.out.println("Warning, bucket size for " + index + " (" + previous.size() +
          ") exceeds granularity");
    }*/
    previous.add(data);
    size++;
    return true;
  }

  public Set<T> get(int index) {
    return map.getOrDefault(index, Collections.emptySet());
  }

  public int size() {
    return size;
  }

  public int biggestBucket() {
    int maxIndex = -1;
    int maxSize = 0;
    for (Entry<Integer, Set<T>> entry : map.entrySet()) {
      int size = entry.getValue().size();
      if (size > maxSize) {
        maxIndex = entry.getKey();
        maxSize = size;
      }
    }
    return maxIndex;
  }
}
