package com.anbillon.routine;

/**
 * Typically filters add, remove rules on matcher.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
public interface Filter {

  Matcher filter(Chain chain);

  interface Chain {

    Matcher matcher();

    Matcher proceed(Matcher matcher);
  }
}
