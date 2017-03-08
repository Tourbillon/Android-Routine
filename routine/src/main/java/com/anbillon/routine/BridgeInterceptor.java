package com.anbillon.routine;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import java.net.URISyntaxException;
import java.util.Set;

import static android.content.Intent.ACTION_VIEW;
import static android.content.Intent.CATEGORY_BROWSABLE;

/**
 * Bridges from application code to router code. It builds an {@link Intent} for router.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
final class BridgeInterceptor implements Interceptor {

  @Override public Router intercept(Chain chain) {
    Router router = chain.router();
    Router.Builder builder = router.newBuilder();
    String schemeUrl = router.schemeUrl();
    Intent intent = router.intent();
    Context context = router.context();
    Class<?> page = router.page();
    String pageName = router.pageName();

    /* check the method to build intent */
    switch (router.method()) {
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

    return chain.proceed(builder.intent(intent).build());
  }
}
