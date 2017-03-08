package com.anbillon.routine;

import java.lang.reflect.Type;

/**
 * A handler to handle parameters with annotation(e.g @Extra, @ExtraSet).
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
abstract class ParameterHandler<T> {
  abstract void apply(RouterBuilder builder, T value) throws IllegalArgumentException;

  static final class Caller<T> extends ParameterHandler<T> {
    private final Routine routine;

    public Caller(Routine routine) {
      this.routine = routine;
    }

    @Override void apply(RouterBuilder builder, T value) throws IllegalArgumentException {
      if (value == null) {
        throw new IllegalArgumentException("Caller parameter value must not be null.");
      }

      builder.caller(routine.resolver(value));
    }
  }

  static final class RequestCode extends ParameterHandler<Integer> {
    @Override void apply(RouterBuilder builder, Integer value) throws IllegalArgumentException {
      if (value == null || value == -1) {
        throw new IllegalArgumentException("RequestCode parameter value must not be null or -1.");
      }

      builder.requestCode(value);
    }
  }

  static final class SchemeUrl extends ParameterHandler<String> {
    @Override void apply(RouterBuilder builder, String value) throws IllegalArgumentException {
      if (value == null) {
        throw new IllegalArgumentException("SchemeUrl parameter value must not be null");
      }

      builder.schemeUrl(value);
    }
  }

  static final class Extra<T> extends ParameterHandler<T> {
    private final String name;
    private final Type type;

    public Extra(String name, Type type) {
      this.name = name;
      this.type = type;
    }

    @Override void apply(RouterBuilder builder, T value) throws IllegalArgumentException {
      if (value == null) {
        throw new IllegalArgumentException(
            "Extra parameter \"" + name + "\" value must not be null.");
      }

      builder.putExtra(name, type, value);
    }
  }

  static final class ExtraSet<T> extends ParameterHandler<T> {
    @Override void apply(RouterBuilder builder, T value) throws IllegalArgumentException {
      if (value == null) {
        throw new IllegalArgumentException("ExtraSet parameter value must not be null.");
      }

      builder.putExtras(value);
    }
  }
}
