package com.anbillon.routine.app;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Open a new page via page class. The is same with the normal method to open a new page.
 * <p>
 * For example:
 * <pre><code>
 * {@code} @Page(DemoActivity.class)
 * {@code} void navigateToDemo();
 * </code></pre>
 * </p>
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 * @see SchemeUrl
 * @see PageName
 */
@Documented @Target(METHOD) @Retention(RUNTIME) public @interface Page {
  /**
   * The value of page.
   */
  Class<?> value();
}
