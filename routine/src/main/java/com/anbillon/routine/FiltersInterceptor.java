package com.anbillon.routine;

import android.content.Intent;

/**
 * A Routine interceptor which filters wanted {@link Intent} according to scheme url.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
final class FiltersInterceptor implements Interceptor {
  private final Matcher matcher;

  FiltersInterceptor(Matcher matcher) {
    this.matcher = matcher;
  }

  @Override public Router intercept(Chain chain) {
    Router router = chain.router();
    if (router.method() != Method.SCHEME_URL) {
      return chain.proceed(router);
    }

    Intent intent = matcher.match(router.context(), router.schemeUrl());
    if (intent == null) {
      return chain.proceed(router);
    }
    intent.putExtras(router.intent());

    return chain.proceed(router.newBuilder().intent(intent).build());
  }
}