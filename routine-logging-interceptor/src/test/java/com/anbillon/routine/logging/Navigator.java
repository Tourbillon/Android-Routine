package com.anbillon.routine.logging;

import android.content.Context;
import android.content.Intent;
import com.anbillon.routine.app.Caller;
import com.anbillon.routine.app.Extra;
import com.anbillon.routine.app.Flags;
import com.anbillon.routine.app.Page;
import com.anbillon.routine.app.PageName;
import com.anbillon.routine.app.RequestCode;
import com.anbillon.routine.app.SchemeUrl;

/**
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
public interface Navigator {
  String SCHEME_URL = "demo://test/login?id=2";
  String PAGE_NAME = "com.anbillon.routine.logging.TestActivity";
  String EXTRA_ID = "com.msxf.EXTRA_ID";

  @SchemeUrl(SCHEME_URL) void navigateWithSchemeUrl(@Caller Context context);

  @PageName(PAGE_NAME) @Flags(Intent.FLAG_ACTIVITY_CLEAR_TOP) void navigateWithPageName(
      @Caller Context context, @Extra(EXTRA_ID) int id, @RequestCode int requestCode);

  @Page(TestActivity.class) void navigateWithPage(@Caller Context context);
}
