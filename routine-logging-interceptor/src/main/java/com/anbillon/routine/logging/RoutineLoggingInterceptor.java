package com.anbillon.routine.logging;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import com.anbillon.routine.Interceptor;
import com.anbillon.routine.Router;

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

    logger.log("-->" + ' ' + router.method() + ' ' + router.target());
    logger.log("From: " + router.context().getClass().getCanonicalName());
    ActivityInfo activityInfo =
        router.intent().resolveActivityInfo(router.context().getPackageManager(), 0);
    logger.log("To: " + (activityInfo != null ? activityInfo.name : "No target page found"));
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
        params.append(extras.get(keys[k].toString()));
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
