package com.github.romainbrenguier.story;


import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.util.ArrayList;
import java.util.List;

public class EventTemplate {
    /* Template may contain special strings: M1, M2 for the full name of the protagonists, P1, P2
    * for the corresponding pronouns. Q1, Q2 for possessive pronouns. */
    String template;
    Multimap<Integer, Condition> preconditions = ArrayListMultimap.create();
    List<Condition> outcome;
    int nbProtagonists = 2;

    EventTemplate template(String s) {
        template = s;
        return this;
    }
    EventTemplate precondition(int protagonist, Condition c) {
        preconditions.get(protagonist).add(c);
        return this;
    }
    EventTemplate outcome(Condition c) {
        outcome.add(c);
        return this;
    }
    EventTemplate protagonists(int nbProtagonists) {
        this.nbProtagonists = nbProtagonists;
        return this;
    }

    static List<EventTemplate> templates() {
        final List<EventTemplate> result = new ArrayList<>();

        result.add(new EventTemplate().template("M1 was a loyal servant to her " +
                "employer, M2. One day, M2 found herself" +
                " in a particularly difficult situation. P2 had been invited to a masquerade " +
                "ball, but had no suitable gown to wear. M1, however, was determined to help her " +
                "employer. She worked tirelessly for days, sewing and stitching a beautiful gown" +
                " for M2. When the night of the ball arrived, M2 was delighted with the" +
                " gown M1 had made for Q2. P2 felt confident and beautiful in it, and P2 " +
                "thanked M2 for her loyalty and dedication.")
                .precondition(2, Condition.isWoman())
                .precondition(1, Condition.hasStatus(Status.Servant))
                .precondition(2, Condition.hasStatus(Status.Aristocrat))
        );
        result.add(new EventTemplate().template("M1 was the loyal servant of the Marquis de " +
                        "Villemont. P1 had been with him since he was a child and had been with " +
                        "him through thick and thin. One day, when M2 was walking in the " +
                        "Tuileries Gardens, he was set upon by a group of thugs. M1, who had been" +
                        " following him from a distance, rushed to his aid and fought off the attackers with only her bare hands. Her bravery and loyalty to the Marquis saved him from certain harm. From that day onwards, the Marquis held her in the highest esteem and she was rewarded for her loyalty with a generous pension.")
                .precondition(1, Condition.hasStatus(Status.Servant))
                .precondition(2, Condition.hasStatus(Status.Aristocrat)));

        result.add(new EventTemplate().template(
        "a former soldier who had " +
                "served in the French army " +
                "during the " +
                "Napoleonic Wars. Pierre had been a brave and fearless soldier, " +
                "known for his unwavering courage and determination on the battlefield. " +
                "But when the war ended, he found himself struggling to adjust to civilian life." +
                " He missed the sense of purpose and camaraderie he had felt as a soldier and found " +
                "himself plagued by nightmares and memories of the horrors he had witnessed."));
        result.add(new EventTemplate().template(
               "wife, who has always lived in the shadow of her husband's heroic deeds. She longs " +
                    "for wealth and status, and would do anything to ensure her family's " +
                       "financial security."));
        result.add(new EventTemplate().template(
            "A struggling artist who is envious of the success of his rivals. He is deeply in " +
                    "debt and sees the murders as an opportunity to steal valuable art and sell " +
                    "it for a high price."));
        result.add(new EventTemplate().template(
            "A wealthy aristocrat who is heavily involved in gambling and has a reputation for " +
                    "being ruthless in his pursuit of money"));
        return result;
    }
}
