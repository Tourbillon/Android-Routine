package com.anbillon.routine;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * A router call which creates the router or invokes the router.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
final class RouterCall {
  private final RouterMethod routerMethod;
  private final List<Interceptor> interceptors;
  private final Object[] args;

  RouterCall(RouterMethod routerMethod, List<Interceptor> interceptors, Object[] args) {
    this.routerMethod = routerMethod;
    this.interceptors = interceptors;
    this.args = args;
  }

  Object create() {
    Router originCall = routerMethod.toRouter(args);

    /* build a full stack of interceptors */
    List<Interceptor> fullInterceptors = new ArrayList<>();
    fullInterceptors.add(new RealInterceptor());
    fullInterceptors.addAll(interceptors);
    Interceptor.Chain chain = new InterceptorChain(fullInterceptors, 0, originCall);
    /* proceed the chain to get real router call */
    Router realCall = chain.proceed(originCall);
    Type returnType = routerMethod.returnType();
    if (returnType == void.class) {
      realCall.start();
    } else if (returnType == Router.class) {
      return realCall;
    } else {
      throw new IllegalStateException("Unsupported return type.");
    }

    return null;
  }
}
