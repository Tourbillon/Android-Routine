package com.anbillon.routine.app;

import android.content.Intent;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * A set of extended data added to the {@link Intent}. The value and keys must include a package
 * prefix, for example the app com.android.contacts would use keys like "com.android.contacts.ID".
 * <p>
 * Simple example:
 * <pre><value>
 *   void navigateToContacts(@ExtraSet("com.android.IDS") Bundle ids);
 * </value></pre>
 * If there's only one set of extended data, then you can just ignore:
 * <pre><value>
 * void navigateToContacts(@ExtraSet Bundle ids);
 * void navigateToContacts(@ExtraSet("com.android.ORIGIN") Intent intent);
 * </value></pre>
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 * @see Extra
 */
@Documented @Target(PARAMETER) @Retention(RUNTIME) public @interface ExtraSet {
  /**
   * The value of the extended data, with package prefix.
   */
  String name() default "";
}
