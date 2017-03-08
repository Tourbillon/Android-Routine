package com.anbillon.routine.app;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Open a new page via page destination. The destination must be a full destination of the page.
 * <p>
 * For example:
 * <pre><code>
 * {@code} @PageName("com.exmaple.DemoActivity")
 * {@code} void navigateToDemo();
 * </code></pre>
 * </p>
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 * @see SchemeUrl
 * @see Page
 */
@Documented @Target(METHOD) @Retention(RUNTIME) public @interface PageName {
  /**
   * The value of page destination.
   */
  String value() default "";
}
