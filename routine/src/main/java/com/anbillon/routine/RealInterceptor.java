package com.anbillon.routine;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.anbillon.routine.Utils.resolveActivityInfo;

/**
 * A Routine interceptor which checks if the {@link Intent} is avaliable. This will be the final
 * interceptor and return real router.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
final class RealInterceptor implements Interceptor {

  @Override public Router intercept(Chain chain) {
    Router router = chain.router();
    Context context = router.context();
    Intent intent = router.intent();
    Class<?> errorPage = router.errorPage();

    if (resolveActivityInfo(context, intent) != null) {
      return router;
    }
    intent = replaceWithErrorPage(context, intent, errorPage);

    return router.newBuilder().intent(intent).build();
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
