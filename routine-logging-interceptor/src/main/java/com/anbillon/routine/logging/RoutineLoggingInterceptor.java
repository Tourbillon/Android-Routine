package com.anbillon.routine.logging;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import com.anbillon.routine.Interceptor;
import com.anbillon.routine.Router;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Arrays;

/**
 * A Routine interceptor which logs router call information.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
public final class RoutineLoggingInterceptor implements Interceptor {
  private Logger logger;
  private volatile Level level = Level.NONE;

  public RoutineLoggingInterceptor() {
    this(Logger.DEFAULT);
  }

  public RoutineLoggingInterceptor(Logger logger) {
    this.logger = logger;
  }

  /**
   * Change the level at which this interceptor logs.
   *
   * @param level {@link Level}
   * @return this object for further chaining
   */
  public RoutineLoggingInterceptor setLevel(Level level) {
    if (level == null) throw new NullPointerException("level == null. Use Level.NONE instead.");
    this.level = level;
    return this;
  }

  @Override public Router intercept(Chain chain) {
    Router router = chain.router();

    if (level == Level.NONE) {
      return chain.proceed(router);
    }

    logger.log("-->" + ' ' + router.method() + ' ' + router.destination());
    logger.log("From: " + router.origin());
    ActivityInfo activityInfo =
        router.intent().resolveActivityInfo(router.context().getPackageManager(), 0);
    logger.log("To: " + (activityInfo != null ? activityInfo.name : "No destination page found"));
    if (router.intent().getFlags() > 0) {
      logger.log("Flags: " + "0x" + Integer.toHexString(router.intent().getFlags()));
    }
    if (router.requestCode() >= 0) {
      logger.log("RequestCode: " + "0x" + Integer.toHexString(router.requestCode()));
    }

    Bundle extras = router.intent().getExtras();
    if (extras != null) {
      StringBuilder params = new StringBuilder();
      params.append("Extras: { ");
      Object keys[] = extras.keySet().toArray();
      for (int k = 0; k < keys.length; k++) {
        params.append(keys[k]);
        params.append("=");
        Object value = extras.get(keys[k].toString());
        params.append(valueToString(value));

        if (k < keys.length - 1) {
          params.append(", ");
        }
      }
      params.append(" }");
      logger.log(params.toString());
    }

    logger.log("<-- END" + ' ' + router.method());

    return chain.proceed(router);
  }

  private String valueToString(Object value) {
    if (value == null) {
      return null;
    }

    if (!value.getClass().isArray()) {
      return value.toString();
    }

    Type type = value.getClass();
    if (type == boolean[].class) {
      return Arrays.toString((boolean[]) value);
    } else if (type == byte[].class) {
      return Arrays.toString((byte[]) value);
    } else if (type == char[].class) {
      return Arrays.toString((char[]) value);
    } else if (type == CharSequence[].class) {
      return Arrays.toString((CharSequence[]) value);
    } else if (type == double[].class) {
      return Arrays.toString((double[]) value);
    } else if (type == float[].class) {
      return Arrays.toString((float[]) value);
    } else if (type == int[].class) {
      return Arrays.toString((int[]) value);
    } else if (type == short[].class) {
      return Arrays.toString((short[]) value);
    } else if (type == long[].class) {
      return Arrays.toString((long[]) value);
    } else if (type == String[].class) {
      return Arrays.toString((String[]) value);
    } else if (type == Parcelable[].class) {
      return Arrays.toString((Parcelable[]) value);
    } else if (type == Serializable[].class) {
      return Arrays.toString((Serializable[]) value);
    } else {
      return value.toString();
    }
  }

  public enum Level {
    /**
     * No logs.
     */
    NONE,

    /**
     * Logs `from`, `to`, extended data and flags.
     *
     * <p>Example:
     * <pre>{@code
     * --> SCHEME URL demo://test/login?id=2
     *
     * From: com.example.DemoActivity
     * To: com.example.TargetActivity
     * Flags: 0x00000001
     *
     * <-- END SCHEME URL
     * }</pre>
     */
    ALL,
  }

  /**
   * A simple indirection for logging debug messages.
   *
   * @author Vincent Cheung (coolingfall@gmail.com)
   */
  public interface Logger {
    Logger DEFAULT = new Logger() {
      @Override public void log(String message) {
        Log.d("RoutineLogging", message);
      }
    };

    /**
     * Output log with given message.
     *
     * @param message message
     */
    void log(String message);
  }
}
