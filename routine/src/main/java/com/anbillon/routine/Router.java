package com.anbillon.routine;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * A router. Instances of this class are immutable.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
public final class Router {
  private final String method;
  private final String target;
  private final String schemeUrl;
  private final String pageName;
  private final Class<?> page;
  private final Class<?> errorPage;
  private final Context context;
  private final Intent intent;
  private final int requestCode;

  private Router(Builder builder) {
    this.method = builder.method;
    this.target = builder.target;
    this.schemeUrl = builder.schemeUrl;
    this.pageName = builder.pageName;
    this.page = builder.page;
    this.errorPage = builder.errorPage;
    this.context = builder.context;
    this.intent = builder.intent;
    this.requestCode = builder.requestCode;
  }

  /**
   * Start current router call to open new page.
   */
  public void start() {
    boolean isActivity = context instanceof Activity;

    try {
      if (requestCode >= 0) {
        if (isActivity) {
          ((Activity) context).startActivityIfNeeded(intent, requestCode);
        }
      } else {
        if (isActivity) {
          ((Activity) context).startActivityIfNeeded(intent, -1);
        } else {
          intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
          context.startActivity(intent);
        }
      }
    } catch (ActivityNotFoundException ignore) {
    }
  }

  public String method() {
    return method;
  }

  public String target() {
    return target;
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
    return context;
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
    private String method;
    private String target;
    private String schemeUrl;
    private String pageName;
    private Class<?> page;
    private Class<?> errorPage;
    private Context context;
    private Intent intent;
    private int requestCode;

    Builder() {
    }

    Builder(Router call) {
      this.method = call.method;
      this.target = call.target;
      this.schemeUrl = call.schemeUrl;
      this.pageName = call.pageName;
      this.page = call.page;
      this.errorPage = call.errorPage;
      this.context = call.context;
      this.intent = call.intent;
      this.requestCode = call.requestCode;
    }

    Builder method(String method) {
      this.method = method;
      return this;
    }

    Builder target(String target) {
      this.target = target;
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

    Builder context(Context context) {
      this.context = context;
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
