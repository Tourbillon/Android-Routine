package com.anbillon.routine.sample.interceptor;

import android.content.Intent;
import com.anbillon.routine.Interceptor;
import com.anbillon.routine.Router;
import com.anbillon.routine.sample.ui.IdentityActivity;

import static com.anbillon.routine.sample.Utils.random;

/**
 * A sample to show an implementation of identity interceptor.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
public final class IdentityInterceptor implements Interceptor {

  @Override public Router intercept(Chain chain) {
    Router router = chain.router();
    Router.Builder builder = router.newBuilder();
    if (random(5) == 0) {
      Intent intent = new Intent(router.context(), IdentityActivity.class);
      builder.intent(intent).requestCode(-1);
    }

    return chain.proceed(builder.build());
  }
}
