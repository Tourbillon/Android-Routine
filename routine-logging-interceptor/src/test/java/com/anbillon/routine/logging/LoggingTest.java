package com.anbillon.routine.logging;

import android.content.Context;
import com.anbillon.routine.Routine;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;
import org.robolectric.shadows.ShadowLog;

@RunWith(RobolectricTestRunner.class) @Config(constants = BuildConfig.class, sdk = 21)
public class LoggingTest {
  private Context context;
  private Navigator navigator;

  @Before public void setUp() throws Exception {
    ShadowLog.stream = System.out;
    context = ShadowApplication.getInstance().getApplicationContext();
    RoutineLoggingInterceptor loggingInterceptor = new RoutineLoggingInterceptor();
    loggingInterceptor.setLevel(RoutineLoggingInterceptor.Level.ALL);
    Routine routine = new Routine.Builder().addInterceptor(loggingInterceptor).build();
    navigator = routine.create(Navigator.class);
  }

  @Test public void testLogging() throws Exception {
    navigator.navigateWithSchemeUrl(context);
    navigator.navigateWithPageName(context, 2, 1212);
    navigator.navigateWithPage(context);
  }
}