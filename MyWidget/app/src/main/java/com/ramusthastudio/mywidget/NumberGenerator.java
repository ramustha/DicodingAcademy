package com.ramusthastudio.mywidget;

import java.util.Random;

public final class NumberGenerator {
  private NumberGenerator() {}

  public static int Generate(int max) {
    Random random = new Random();
    return random.nextInt(max);
  }
}
