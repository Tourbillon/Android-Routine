package com.anbillon.routine;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.anbillon.routine.Utils.checkNotNull;

/**
 * Routine parses a Java interface to routers by using annotations on the declared methods to
 * define how router are made. Create instances using {@linkplain Builder the builder} and pass
 * your interface to {@link #create} to generate an implementation.
 * <p>
 * For example:
 * <pre><code>
 *   Routine routine = new Routine.Builder()
 *      .errorPage(ErrorActivity.class)
 *      .build();
 *
 *  MyRouter router = routine.create(MyRouter.class);
 *  router.navigateToDemo(contex);
 * </code></pre>
 * </p>
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
public final class Routine {
  private final Map<Method, RouterMethod> routerMethodCache = new ConcurrentHashMap<>();
  private final List<Interceptor> interceptors;
  private final List<Filter> filters;
  private final List<Resolver.Factory> resolverFactories;
  private final Class<?> errorPage;

  private Routine(Builder builder) {
    this.interceptors = Utils.immutableList(builder.interceptors);
    this.filters = Utils.immutableList(builder.filters);
    this.resolverFactories = Utils.immutableList(builder.resolverFactories);
    this.errorPage = builder.errorPage;
  }

  /**
   * Create an implementation of navigator defined by the {@code router} interface.
   * <p>
   * The navigate type for a given method is obtained origin an annotation on the method
   * describing the request type. The built-in methods are {@link com.anbillon.routine.app.SchemeUrl
   * SchemeUrl}, {@link com.anbillon.routine.app.PageName PageName} and {@link
   * com.anbillon.routine.app.Page Page}. For a dynamic shceme url, omit the path on the
   * annotation and annotate the parameter with {@link com.anbillon.routine.app.SchemeUrl
   * SchemeUrl}. If no annotations on method and no SchemeUrl on parameter, exception will occur.
   * <p>
   * You can add custom flags on method with {@link com.anbillon.routine.app.Flags Flags}.
   * <p>
   * Each navigation must include one and only one {@link com.anbillon.routine.app.Caller
   * Caller} whose type is {@link android.content.Context}.
   * <p>
   * You can add extended data in call by adding annotation on parameters with {@link
   * com.anbillon.routine.app.Extra Extra} and {@link com.anbillon.routine.app.ExtraSet
   * ExtraSet}. One or more extras can be added in.
   * <p>
   * If you want navigation with result, then add annotation on parameter with {@link
   * com.anbillon.routine.app.RequestCode RequestCode}.
   *
   * @param router router interface
   * @param <T> type of router
   * @return an instance of given router
   */
  @SuppressWarnings("unchecked") public <T> T create(final Class<T> router) {
    Utils.validateRouterInterface(router);

    return (T) Proxy.newProxyInstance(router.getClassLoader(), new Class<?>[] { router },
        new InvocationHandler() {
          @Override public Object invoke(Object proxy, Method method, Object[] args)
              throws Throwable {

            if (method.getDeclaringClass() == Object.class) {
              return method.invoke(this, args);
            }

            RouterMethod routerMethod = loadRouterMethod(method);
            RouterCall routerCall = new RouterCall(routerMethod, interceptors, filters, args);
            return routerCall.create();
          }
        });
  }

  /**
   * Caller resolver to call the router.
   *
   * @param caller caller
   * @param <T> type of caller
   * @return {@link Resolver}
   */
  <T> Resolver resolver(T caller) {
    for (int i = 0, count = resolverFactories.size(); i < count; i++) {
      Resolver.Factory factory = resolverFactories.get(i);
      Resolver resolver = factory.create(caller);
      if (resolver != null) {
        return resolver;
      }
    }

    throw new IllegalArgumentException("No caller found, caller type is not supported.");
  }

  /**
   * The error page to open when an error occured.
   *
   * @return error page clazz
   */
  Class<?> errorPage() {
    return errorPage;
  }

  /**
   * Load {@link RouterMethod} origin cache. Create a new one if there's no result.
   *
   * @param method {@link Method}
   * @return {@link RouterMethod}
   */
  RouterMethod loadRouterMethod(Method method) {
    RouterMethod result = routerMethodCache.get(method);
    if (result != null) {
      return result;
    }

    synchronized (routerMethodCache) {
      result = routerMethodCache.get(method);
      if (result == null) {
        result = new RouterMethod.Builder(this, method).build();
        routerMethodCache.put(method, result);
      }
    }

    return result;
  }

  public static final class Builder {
    private List<Interceptor> interceptors = new ArrayList<>();
    private List<Filter> filters = new ArrayList<>();
    private List<Resolver.Factory> resolverFactories = new ArrayList<>();
    private Class<?> errorPage;

    public Builder() {
      resolverFactories.add(new BuiltInResolverFactories());
    }

    /**
     * Add one {@link Resolver.Factory} into routine.
     *
     * @param factory {@link Resolver.Factory}
     * @return this object for further chaining
     */
    public Builder addResolverFactory(Resolver.Factory factory) {
      resolverFactories.add(checkNotNull(factory, "factory == null"));
      return this;
    }

    /**
     * Add one {@link Interceptor} into routine.
     *
     * @param interceptor {@link Interceptor}
     * @return this object for further chaining
     */
    public Builder addInterceptor(Interceptor interceptor) {
      interceptors.add(interceptor);
      return this;
    }

    /**
     * Add one {@link Filter} into routine.
     *
     * @param filter {@link Filter}
     * @return this object for further chaining
     */
    public Builder addFilter(Filter filter) {
      filters.add(filter);
      return this;
    }

    /**
     * Navigate to a error page when the given page is not found.
     *
     * @param errorPage error page
     * @return this builder for further chaining
     */
    public Builder errorPage(Class<?> errorPage) {
      this.errorPage = errorPage;
      return this;
    }

    public Routine build() {
      return new Routine(this);
    }
  }
}
