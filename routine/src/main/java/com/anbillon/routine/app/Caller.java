package com.anbillon.routine.app;

import android.app.Activity;
import android.content.Context;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * The caller which will open the given page. The value must be {@link Context} or it's child
 * class which is able to open {@link Activity}.
 * <p>
 * For example:
 * <pre><code>
 * void navigateToDemo(@Caller Context context);
 * </code></pre>
 * </p>
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
@Documented @Target(PARAMETER) @Retention(RUNTIME) public @interface Caller {
}
