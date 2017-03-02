package com.anbillon.routine.app;

import android.content.Intent;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Additional value to add to the {@link Intent} (or with existing value). If {@link #set()} was
 * set to true, then {@link Intent#setFlags(int)} will be invoked, otherwise {@link
 * Intent#addFlags(int)} will be invoked.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
@Documented @Target(METHOD) @Retention(RUNTIME) public @interface Flags {
  /**
   * Additional value to add.
   */
  int value() default 0;

  /**
   * Use setFlags or addFlags.
   */
  boolean set() default false;
}
