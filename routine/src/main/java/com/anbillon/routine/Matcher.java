package com.anbillon.routine;

import android.content.Context;
import android.content.Intent;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.anbillon.routine.Utils.resolveActivityInfo;
import static com.anbillon.routine.Utils.resolveSchemeUrl;

/**
 * A matcher to match scheme url. Instances of this class are immutable.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
public final class Matcher {
  private final Map<String, Set<Class<?>>> pagesMap;

  private Matcher(Builder builder) {
    this.pagesMap = builder.pagesMap;
  }

  /**
   * Match an {@link Intent} with given scheme url.
   *
   * @param schemeUrl scheme url
   * @return {@link Intent} if matched, otherwise return null.
   */
  public Intent match(Context context, String schemeUrl) {
    Set<Class<?>> pages = null;
    for (String key : pagesMap.keySet()) {
      if (schemeUrl.contains(key)) {
        pages = pagesMap.get(key);
        break;
      }
    }

    if (pages != null && !pages.isEmpty()) {
      Intent intent = new Intent();
      for (Class<?> page : pages) {
        intent.setClass(context, page);
        if (resolveActivityInfo(context, intent) != null) {
          return intent;
        }
      }
    }

    return null;
  }

  public Builder newBuilder() {
    return new Builder(this);
  }

  public static final class Builder {
    private Map<String, Set<Class<?>>> pagesMap = new ConcurrentHashMap<>();

    Builder() {
    }

    Builder(Matcher matcher) {
      this.pagesMap = matcher.pagesMap;
    }

    public Builder addPage(String schemeUrl, Class<?> page) {
      String realSchemeUrl = resolveSchemeUrl(schemeUrl);
      Set<Class<?>> pages = pagesMap.get(realSchemeUrl);
      if (pages != null && !pages.isEmpty()) {
        pages.add(page);
      } else {
        HashSet<Class<?>> newPages = new HashSet<>();
        newPages.add(page);
        pagesMap.put(schemeUrl, newPages);
      }

      return this;
    }

    public Builder addPageName(String schemeUrl, String pageName) {
      try {
        return addPage(schemeUrl, Class.forName(pageName));
      } catch (ClassNotFoundException ignore) {
      }

      return this;
    }

    public Matcher build() {
      return new Matcher(this);
    }
  }
}
