package com.github.romainbrenguier.story;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface Condition {
    class AgeCondition implements Condition { int min; int max; }
    class SexCondition implements Condition { boolean isWoman; }
    class StatusCondition implements Condition {
        List<Status> allowed; }
    class AndCondition implements Condition { List<Condition> conjunct; }
    class OrCondition implements Condition { List<Condition> disjunct; }

    static Condition isWoman() {
        final SexCondition sexCondition = new SexCondition();
        sexCondition.isWoman = true;
        return sexCondition;
    }
    static Condition isMan() {
        final SexCondition sexCondition = new SexCondition();
        sexCondition.isWoman = false;
        return sexCondition;
    }
    static Condition hasStatus(Status... statuses) {
        final ArrayList<Status> list = new ArrayList<>(Arrays.asList(statuses));
        final StatusCondition condition = new StatusCondition();
        condition.allowed = list;
        return condition;
    }
}
