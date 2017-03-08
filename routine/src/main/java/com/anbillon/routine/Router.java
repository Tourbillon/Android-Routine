package com.anbillon.routine;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.anbillon.routine.Utils.checkNotNull;

/**
 * A router. Instances of this class are immutable.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
public final class Router {
  private final Method method;
  private final String destination;
  private final String schemeUrl;
  private final String pageName;
  private final Class<?> page;
  private final Class<?> errorPage;
  private final Resolver resolver;
  private final Intent intent;
  private final int requestCode;

  private Router(Builder builder) {
    this.method = builder.method;
    this.destination = builder.target;
    this.schemeUrl = builder.schemeUrl;
    this.pageName = builder.pageName;
    this.page = builder.page;
    this.errorPage = builder.errorPage;
    this.resolver = builder.resolver;
    this.intent = checkNotNull(builder.intent, "intent == null in Router");
    this.requestCode = builder.requestCode;
  }

  /**
   * Start current router call to open new page.
   */
  public boolean start() {
    try {
      if (requestCode >= 0) {
        resolver.startActivityForResult(intent, requestCode);
      } else {
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
        resolver.startActivity(intent);
      }
    } catch (ActivityNotFoundException ignore) {
      return false;
    }

    return true;
  }

  public Method method() {
    return method;
  }

  public String origin() {
    return resolver.callerName();
  }

  public String destination() {
    return destination;
  }

  String schemeUrl() {
    return schemeUrl;
  }

  String pageName() {
    return pageName;
  }

  Class<?> page() {
    return page;
  }

  Class<?> errorPage() {
    return errorPage;
  }

  public Context context() {
    return resolver.context();
  }

  public Intent intent() {
    return intent;
  }

  public int requestCode() {
    return requestCode;
  }

  public Builder newBuilder() {
    return new Builder(this);
  }

  public static final class Builder {
    private Method method;
    private String target;
    private String schemeUrl;
    private String pageName;
    private Class<?> page;
    private Class<?> errorPage;
    private Resolver resolver;
    private Intent intent;
    private int requestCode;

    Builder() {
    }

    Builder(Router router) {
      this.method = router.method;
      this.target = router.destination;
      this.schemeUrl = router.schemeUrl;
      this.pageName = router.pageName;
      this.page = router.page;
      this.errorPage = router.errorPage;
      this.resolver = router.resolver;
      this.intent = router.intent;
      this.requestCode = router.requestCode;
    }

    Builder method(Method method) {
      this.method = method;
      return this;
    }

    Builder destination(String destination) {
      this.target = destination;
      return this;
    }

    Builder schemeUrl(String schemeUrl) {
      this.schemeUrl = schemeUrl;
      return this;
    }

    Builder pageName(String pageName) {
      this.pageName = pageName;
      return this;
    }

    Builder page(Class<?> page) {
      this.page = page;
      return this;
    }

    Builder errorPage(Class<?> errorPage) {
      this.errorPage = errorPage;
      return this;
    }

    Builder resolver(Resolver resolver) {
      this.resolver = resolver;
      return this;
    }

    public Builder intent(Intent intent) {
      this.intent = intent;
      return this;
    }

    public Builder requestCode(int requestCode) {
      this.requestCode = requestCode;
      return this;
    }

    public Router build() {
      return new Router(this);
    }
  }
}
