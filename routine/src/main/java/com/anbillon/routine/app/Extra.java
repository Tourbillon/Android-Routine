package com.anbillon.routine.app;

import android.content.Intent;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Extended data added to the {@link Intent}. The value must include a package prefix, for example
 * the app com.android.contacts would use names like "com.android.contacts.ShowAll".
 * <p>
 * For example:
 * <pre><value>
 *   void navigateToContacts(@Extra("com.android.id") long id);
 * </value></pre>
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 * @see ExtraSet
 */
@Documented @Target(PARAMETER) @Retention(RUNTIME) public @interface Extra {
  /**
   * The value of the extended data, with package prefix.
   */
  String value() default "";
}
