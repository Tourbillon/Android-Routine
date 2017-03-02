package com.anbillon.routine.sample;

import java.util.Random;

/**
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
public final class Utils {
  /**
   * Get the random number according to the cardinal number.
   *
   * @param cardinal the cardinal number, the max random number will be (cardinal-1)
   * @return the random number
   */
  public static int random(int cardinal) {
    Random random = new Random();
    return cardinal == 0 ? 0 : Math.abs(random.nextInt()) % cardinal;
  }
}
