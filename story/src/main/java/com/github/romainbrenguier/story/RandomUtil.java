package com.github.romainbrenguier.story;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class RandomUtil {

    @Nullable
    public static <T> T nextInList(Random r, List<T> list) {
        if (list.isEmpty()) return null;
        return list.get(r.nextInt(list.size()));
    }
}
