package com.anbillon.routine;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import java.net.URISyntaxException;
import java.util.Set;

import static android.content.Intent.ACTION_VIEW;
import static android.content.Intent.CATEGORY_BROWSABLE;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.anbillon.routine.Utils.resolveActivityInfo;

/**
 * A Routine interceptor which creates a real {@link Intent}.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
final class RealInterceptor implements Interceptor {

  @Override public Router intercept(Chain chain) {
    Router call = chain.router();
    Router.Builder builder = call.newBuilder();
    String schemeUrl = call.schemeUrl();
    Intent intent = call.intent();
    Context context = call.context();
    Class<?> page = call.page();
    Class<?> errorPage = call.errorPage();
    String pageName = call.pageName();

    /* check scheme url first, then page target and last is page */
    switch (call.method()) {
      case SCHEME_URL:
        try {
          Uri uri = Intent.parseUri(schemeUrl, 0).getData();
          Set<String> names = uri.getQueryParameterNames();
          if (names.size() > 0) {
            Bundle extras = new Bundle();
            for (String name : names) {
              extras.putString(name, uri.getQueryParameter(name));
            }
            intent.putExtras(extras);
          }
          intent.setAction(ACTION_VIEW)
              .setData(uri.buildUpon().clearQuery().build())
              .addCategory(CATEGORY_BROWSABLE)
              .setComponent(null);
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            intent.setSelector(null);
          }
        } catch (URISyntaxException ignore) {
        }
        break;

      case PAGE_NAME:
        try {
          intent.setClass(context, Class.forName(pageName));
        } catch (ClassNotFoundException ignore) {
        }
        break;

      case PAGE:
        intent.setClass(context, page);
        break;

      default:
        break;
    }

    if (resolveActivityInfo(context, intent) == null) {
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
