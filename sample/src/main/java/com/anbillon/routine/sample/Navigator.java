package com.anbillon.routine.sample;

import android.content.Context;
import android.content.Intent;
import com.anbillon.routine.app.Caller;
import com.anbillon.routine.app.Extra;
import com.anbillon.routine.app.Flags;
import com.anbillon.routine.app.Page;
import com.anbillon.routine.app.PageName;
import com.anbillon.routine.app.RequestCode;
import com.anbillon.routine.app.SchemeUrl;
import com.anbillon.routine.sample.ui.DemoActivity;
import com.anbillon.routine.sample.ui.HtmlActivity;

/**
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
public interface Navigator {
  String EXTRA_ID = "com.msxf.EXTRA_ID";

  /**
   * Navigate to {@link DemoActivity} with scheme url.
   *
   * @param context context to use
   */
  @SchemeUrl("demo://test/login?id=2") void navigateToDemoWithSchemeUrl(@Caller Context context);

  /**
   * Navigate to {@link DemoActivity} with activity page value.
   *
   * @param context context to use
   */
  @PageName("com.anbillon.routine.sample.ui.DemoActivity") @Flags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
  void navigateToDemoWithPageName(@Caller Context context, @Extra(EXTRA_ID) String id,
      @RequestCode int requestCode);

  /**
   * Navigate to {@link DemoActivity} with activity page.
   *
   * @param context context to use
   */
  @Page(DemoActivity.class) void navigateToDemoWithPage(@Caller Context context);

  /**
   * Navigate to a page but not found.
   *
   * @param context context to use
   */
  @PageName("com.msxf.DemoActivity") void navigateWithNotFound(@Caller Context context);

  /**
   * Navigate to {@link HtmlActivity} with activity page.
   *
   * @param context context to use
   */
  @Page(HtmlActivity.class) void navigateToHtml(@Caller Context context);

  /**
   * Navigate with dynamic scheme url.
   *
   * @param context context to use
   * @param url dynamic shceme url
   */
  void navigateWithDynamicSchemeUrl(@Caller Context context, @SchemeUrl String url);
}
