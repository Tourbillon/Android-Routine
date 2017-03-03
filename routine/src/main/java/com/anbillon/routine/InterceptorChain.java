package com.anbillon.routine;

import java.util.List;

/**
 * A concrete interceptor chain that carries the entire interceptor chain.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
final class InterceptorChain implements Interceptor.Chain {
  private final List<Interceptor> interceptors;
  private final Router router;
  private final int index;
  private int calls;

  InterceptorChain(List<Interceptor> interceptors, int index, Router router) {
    this.interceptors = interceptors;
    this.index = index;
    this.router = router;
  }

  @Override public Router router() {
    return router;
  }

  @Override public Router proceed(Router router) {
    if (router == null) {
      throw new IllegalArgumentException("Router in interceptor can not be null.");
    }

    if (index == 0 && interceptors.size() == 0 || index >= interceptors.size()) {
      return router;
    }

    calls++;

    /* call the next interceptor in the chain */
    InterceptorChain next = new InterceptorChain(interceptors, index + 1, router);
    Interceptor interceptor = interceptors.get(index);
    Router result = interceptor.intercept(next);

    /* confirm that the next interceptor made its required call to chain.proceed() */
    if (index + 1 < interceptors.size() && next.calls != 1) {
      throw new IllegalStateException(
          "Routine interceptor " + interceptor + " must call proceed() exactly once.");
    }

    return result;
  }
}
