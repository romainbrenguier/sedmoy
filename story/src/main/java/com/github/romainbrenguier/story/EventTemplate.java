package com.github.romainbrenguier.story;

import java.util.Arrays;
import java.util.List;

public class EventTemplate {

    final String template;

    // Example of a background story: (ambition)
    // Born into a family of modest means, Victor had always dreamed of making a name for himself
    // in the Parisian art scene. He had studied under some of the city's most esteemed artists,
    // but despite his talent, he struggled to gain recognition. Frustrated and desperate,
    // Victor turned to absinthe, seeking inspiration and escape from the harsh realities of his
    // life. But his addiction only made things worse. He lost his job as a gallery assistant,
    // was evicted from his apartment, and found himself living on the streets. It was then that
    // Victor's ambition took a dark turn. He became obsessed with the idea of making a name for
    // himself, no matter the cost. He began stealing art supplies and selling counterfeit paintings,
    // hoping to pass them off as his own. But when he was caught in the act of stealing a valuable
    // painting from a gallery, Victor's desperation led him down an even darker path.

    static String courageStory1 = "a former soldier who had served in the French army during the " +
            "Napoleonic Wars. Pierre had been a brave and fearless soldier, " +
            "known for his unwavering courage and determination on the battlefield. " +
            "But when the war ended, he found himself struggling to adjust to civilian life." +
            " He missed the sense of purpose and camaraderie he had felt as a soldier and found " +
            "himself plagued by nightmares and memories of the horrors he had witnessed.";

    static List<String> greedy = Arrays.asList(
            "wife, who has always lived in the shadow of her husband's heroic deeds. She longs " +
                    "for wealth and status, and would do anything to ensure her family's financial security.",
            "A struggling artist who is envious of the success of his rivals. He is deeply in " +
                    "debt and sees the murders as an opportunity to steal valuable art and sell " +
                    "it for a high price.",
            "A wealthy aristocrat who is heavily involved in gambling and has a reputation for " +
                    "being ruthless in his pursuit of money. He may have turned to murder as a " +
                    "way to eliminate his debts and protect his fortune.");

    public EventTemplate(String template) {
        this.template = template;
    }
}
