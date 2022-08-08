package com.github.romainbrenguier.sedmoy.util;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

public class Timing implements Closeable {

    private long startTime;
    private final List<Long> timer;

    public Timing(List<Long> timer) {
        this.timer = timer;
        startTime = System.nanoTime();
    }


    @Override
    public void close() {
        timer.add(System.nanoTime() - startTime);
    }
}
