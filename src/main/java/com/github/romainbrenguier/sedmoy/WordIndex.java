package com.github.romainbrenguier.sedmoy;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class WordIndex {

  private static final Map<Character, Integer> indices = new HashMap();
  static {
    indices.put('t', 100);
    indices.put('т', 100);
    indices.put('д', 150);
    indices.put('н', 200);
    indices.put('м', 250);
    indices.put('а', 300);
    indices.put('a', 300);
    indices.put('е', 320);
    indices.put('э', 320);
    indices.put('и', 340);
    indices.put('й', 340);
    indices.put('о', 360);
    indices.put('o', 360);
    indices.put('у', 380);
    indices.put('u', 380);
    indices.put('ы', 380);
    indices.put('х', 400);
    indices.put('р', 450);
    indices.put('y', 500);
    indices.put('л', 550);
    indices.put('ш', 600);
    indices.put('ж', 650);
    indices.put('к', 700);
    indices.put('г', 750);
    indices.put('ф', 800);
    indices.put('в', 850);
    indices.put('п', 900);
    indices.put('б', 950);
    indices.put('с', 1000);
    indices.put('s', 1000);
    indices.put('з', 1050);
  }

  static final Set<String> PREFIXES_TO_IGNORE = new HashSet<>();
//  static {
//    PREFIXES_TO_IGNORE.add("по");
//    PREFIXES_TO_IGNORE.add("со");
//    PREFIXES_TO_IGNORE.add("ве");
//  }

  static int VOWEL_INDEX = 300;


  /** Replace letters that represent double sounds by equivalents */
  static String replaceLetters(String input) {
    String replaced = input
        .toLowerCase()
        .replace("ц", "ts")
        .replace("ю", "yu")
        .replace("я", "ya")
        .replace("ё", "yo")
        .replace("ч", "tш");

    for (String prefix : PREFIXES_TO_IGNORE) {
      replaced = replaced.replace(prefix, "");
    }
    return replaced;
  }

  static boolean isVowel(char c) {
    int index = indices.getOrDefault(c, 0);
    return VOWEL_INDEX <= index && index < VOWEL_INDEX + 100;
  }

  static Character firstVowel(String word) {
    for (int i = 0; i < word.length(); ++i) {
      if (isVowel(word.charAt(i))) {
        return word.charAt(i);
      }
    }
    return null;
  }

  static Character firstConsonant(String word) {
    for (int i = 0; i < word.length(); ++i) {
      if (!isVowel(word.charAt(i))) {
        return word.charAt(i);
      }
    }
    return null;
  }

  static Character secondConsonant(String word) {
    boolean firstSeen = false;
    for (int i = 0; i < word.length(); ++i) {
      if (!isVowel(word.charAt(i))) {
        if (firstSeen) {
          return word.charAt(i);
        } else {
          firstSeen = true;
        }
      }
    }
    return null;
  }

  public static int of(String word) {
    word = replaceLetters(word);
    Integer first = indices.getOrDefault(word.charAt(0), 0);
    // A
    if (word.length() == 1) {
      return first / 100 + 20;
    }
    Integer second = indices.getOrDefault(word.charAt(1), 300);
    Integer vowelIndex = indices.getOrDefault(firstVowel(word), 300);
    // BA
    if (word.length() == 2) {
      Integer consonantIndex = indices.getOrDefault(firstConsonant(word), 300);
      return consonantIndex / 10 + (vowelIndex - VOWEL_INDEX) / 20 - 10;
    }
    // AB...
    if (isVowel(word.charAt(0))) {
      return first + (second - 1) / 50;
    }
    // BAB...
    Integer consonantIndex = indices.getOrDefault(secondConsonant(word), 300);
    return first + (vowelIndex - VOWEL_INDEX) / 2 + (consonantIndex / 100 - 1);
  }
}
