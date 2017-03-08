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
  private final List<Filter> filters;
  private final Object[] args;

  RouterCall(RouterMethod routerMethod, List<Interceptor> interceptors, List<Filter> filters,
      Object[] args) {
    this.routerMethod = routerMethod;
    this.interceptors = interceptors;
    this.filters = filters;
    this.args = args;
  }

  Object create() {
    Router originRouter = routerMethod.toRouter(args);

    /* build filters with matcher */
    Matcher matcher = new Matcher.Builder().build();
    FilterChain filterChain = new FilterChain(filters, 0, matcher);
    Matcher realMatcher = filterChain.proceed(matcher);
    FiltersInterceptor filtersInterceptor = new FiltersInterceptor(realMatcher);

    /* build a full stack of interceptors */
    List<Interceptor> fullInterceptors = new ArrayList<>();
    fullInterceptors.add(new BridgeInterceptor());
    fullInterceptors.add(filtersInterceptor);
    fullInterceptors.addAll(interceptors);
    fullInterceptors.add(new RealInterceptor());
    Interceptor.Chain interceptorChain = new InterceptorChain(fullInterceptors, 0, originRouter);
    /* proceed the chain to get real router */
    Router realRouter = interceptorChain.proceed(originRouter);
    Type returnType = routerMethod.returnType();
    if (returnType == void.class) {
      realRouter.start();
    } else if (returnType == Router.class) {
      return realRouter;
    } else {
      throw new IllegalStateException("Unsupported return type.");
    }

    return null;
  }
}
