package com.anbillon.routine;

/**
 * A handler to handle method with annotation(e.g @SchemeUrl, @PageName).
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
abstract class MethodHandler<T> {
  protected final T value;

  MethodHandler(T value) {
    this.value = value;
  }

  abstract void apply(RouterBuilder builder) throws IllegalArgumentException;

  static final class SchemeUrl extends MethodHandler<String> {
    SchemeUrl(String value) {
      super(value);
    }

    @Override void apply(RouterBuilder builder) throws IllegalArgumentException {
      if (value == null) {
        throw new IllegalArgumentException("SchemeUrl value must not be null.");
      }

      builder.schemeUrl(value);
    }
  }

  static final class PageName extends MethodHandler<String> {
    public PageName(String value) {
      super(value);
    }

    @Override void apply(RouterBuilder builder) throws IllegalArgumentException {
      if (value == null) {
        throw new IllegalArgumentException("PageName value must not be null.");
      }

      builder.pageName(value);
    }
  }

  static final class Page extends MethodHandler<Class<?>> {
    Page(Class<?> value) {
      super(value);
    }

    @Override void apply(RouterBuilder builder) throws IllegalArgumentException {
      if (value == null) {
        throw new IllegalArgumentException("Page value must not be null.");
      }

      builder.page(value);
    }
  }

  static final class Flags extends MethodHandler<Integer> {
    final boolean set;

    Flags(int value, boolean set) {
      super(value);
      this.set = set;
    }

    @Override void apply(RouterBuilder builder) throws IllegalArgumentException {
      if (value == null) {
        throw new IllegalArgumentException("Flags value must not be null.");
      }

      builder.flags(value, set);
    }
  }
}
