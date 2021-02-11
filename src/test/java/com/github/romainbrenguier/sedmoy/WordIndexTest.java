package com.github.romainbrenguier.sedmoy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class WordIndexTest {

  @Test
  public void testOf() {
    // Arrange, Act and Assert
    assertEquals(109, WordIndex.of("ца"));
    assertEquals(44, WordIndex.of("ю"));
    assertEquals(23, WordIndex.of("а"));
    assertEquals(1, WordIndex.of("те"));
    assertEquals(1050, WordIndex.of("Здравствуй"));
  }

}

