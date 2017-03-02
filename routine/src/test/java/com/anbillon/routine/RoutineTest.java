package com.anbillon.routine;

import android.content.Context;
import android.net.Uri;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;
import org.robolectric.shadows.ShadowLog;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class) @Config(constants = BuildConfig.class, sdk = 21)
public class RoutineTest {
  private Context context;
  private Navigator navigator;

  @Before public void setUp() throws Exception {
    ShadowLog.stream = System.out;
    context = ShadowApplication.getInstance().getApplicationContext();
    Routine routine = new Routine.Builder().build();
    navigator = routine.create(Navigator.class);
  }

  @Test public void testUri() throws Exception {
    Uri uri = Uri.parse("demo://test/login?id=2");
    assertEquals("demo", uri.getScheme());
    assertEquals("test", uri.getHost());
    assertEquals("/login", uri.getPath());
    assertEquals("2", uri.getQueryParameter("id"));
  }

  class ChildClass extends RoutineTest {
  }

  @Test public void testClassName() {
    assertEquals("com.anbillon.routine.RoutineTest", getClass().getCanonicalName());
    RoutineTest childClass = new ChildClass();
    assertEquals("com.anbillon.routine.RoutineTest.ChildClass",
        childClass.getClass().getCanonicalName());
  }

  @Test public void testRoutine() throws Exception {
    navigator.navigateWithSchemeUrl(context);
    navigator.navigateWithPageName(context, 2, 1212);
    navigator.navigateWithPage(context);
  }
}