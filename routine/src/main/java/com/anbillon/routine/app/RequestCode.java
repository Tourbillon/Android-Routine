package com.anbillon.routine.app;

import android.app.Activity;
import android.content.Intent;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Make request value for method {@link Activity#startActivityForResult(Intent, int)}. The code
 * will be returned in onActivityResult() when the activity exits if value >= 0.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 * @see Page
 * @see PageName
 */
@Documented @Target({ METHOD, PARAMETER }) @Retention(RUNTIME) public @interface RequestCode {
  /**
   * The value of request code.
   */
  int value() default -1;
}
