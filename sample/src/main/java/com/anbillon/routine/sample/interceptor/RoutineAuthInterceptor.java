package com.anbillon.routine.sample.interceptor;

import android.content.Intent;
import com.anbillon.routine.Interceptor;
import com.anbillon.routine.Router;
import com.anbillon.routine.RoutineException;
import com.anbillon.routine.sample.ui.LoginActivity;

import static com.anbillon.routine.sample.Utils.random;

/**
 * A sample to show an implementation of authentication interceptor.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
public final class RoutineAuthInterceptor implements Interceptor {

  @Override public Router intercept(Chain chain) throws RoutineException {
    Router router = chain.router();
    Router.Builder builder = router.newBuilder();
    if (random(10) == 0) {
      Intent intent = new Intent(router.context(), LoginActivity.class);
      builder.intent(intent).requestCode(-1);
    }

    return chain.proceed(builder.build());
  }
}
