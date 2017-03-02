package com.anbillon.routine;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import java.net.URISyntaxException;

import static android.content.Intent.ACTION_VIEW;
import static android.content.Intent.CATEGORY_BROWSABLE;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * A Routine interceptor which parses a real {@link Intent}.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
final class RealInterceptor implements Interceptor {

  @Override public Router intercept(Chain chain) {
    Router router = chain.router();
    Router.Builder builder = router.newBuilder();
    String schemeUrl = router.schemeUrl();
    Intent intent = router.intent();
    Context context = router.context();
    Class<?> page = router.page();
    Class<?> errorPage = router.errorPage();
    String pageName = router.pageName();

    /* check scheme url first, then page target and last is page */
    switch (router.method()) {
      case "SCHEME URL":
        try {
          Intent schemeIntent = Intent.parseUri(schemeUrl, 0);
          intent.setAction(ACTION_VIEW)
              .setData(schemeIntent.getData())
              .addCategory(CATEGORY_BROWSABLE)
              .setComponent(null);
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            intent.setSelector(null);
          }
        } catch (URISyntaxException ignore) {
        }
        break;

      case "PAGE NAME":
        try {
          intent.setClass(context, Class.forName(pageName));
        } catch (ClassNotFoundException ignore) {
        }
        break;

      case "PAGE":
        intent.setClass(context, page);
        break;

      default:
        break;
    }

    if (intent.resolveActivityInfo(context.getPackageManager(), 0) == null) {
      intent = replaceWithErrorPage(context, intent, errorPage);
    }

    return chain.proceed(builder.intent(intent).build());
  }

  private Intent replaceWithErrorPage(Context context, Intent origin, Class<?> errorPage) {
    if (errorPage == null) {
      Intent intent =
          context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
      return intent == null ? origin : intent;
    }

    return origin.setClass(context, errorPage)
        .addFlags(context instanceof Activity ? 0 : FLAG_ACTIVITY_NEW_TASK);
  }
}
