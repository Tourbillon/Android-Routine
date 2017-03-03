package com.anbillon.routine;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
final class Utils {

  private Utils() {
    /* no instance */
  }

  /**
   * To check if the given {@code object} is null.
   *
   * @param object object to check
   * @param message message to throw
   * @param <T> type of object
   * @return true if object is null, otherwise return false
   */
  static <T> T checkNotNull(T object, String message) {
    if (object == null) {
      throw new NullPointerException(message);
    }
    return object;
  }

  /**
   * Returns an immutable copy of {@code list}.
   */
  static <T> List<T> immutableList(List<T> list) {
    return Collections.unmodifiableList(new ArrayList<>(list));
  }

  /**
   * Validate the interfaces of router.
   *
   * @param router router interface
   * @param <T> type of router
   */
  static <T> void validateRouterInterface(Class<T> router) {
    if (!router.isInterface()) {
      throw new IllegalArgumentException("API declarations must be interfaces.");
    }

    /*
     * Prevent API interfaces from extending other interfaces. This not only avoids a bug in
     * Android (http://b.android.com/58753) but it forces composition of API declarations which is
     * the recommended pattern.
     */
    if (router.getInterfaces().length > 0) {
      throw new IllegalArgumentException("API interfaces must not extend other interfaces.");
    }
  }

  /**
   * To check if there's unresolvable {@link Type}.
   *
   * @param type {@link Type} to check
   * @return true if has, otherwise return false
   */
  static boolean hasUnresolvableType(Type type) {
    if (type instanceof Class<?>) {
      return false;
    }
    if (type instanceof ParameterizedType) {
      ParameterizedType parameterizedType = (ParameterizedType) type;
      for (Type typeArgument : parameterizedType.getActualTypeArguments()) {
        if (hasUnresolvableType(typeArgument)) {
          return true;
        }
      }
      return false;
    }
    if (type instanceof GenericArrayType) {
      return hasUnresolvableType(((GenericArrayType) type).getGenericComponentType());
    }
    if (type instanceof TypeVariable) {
      return true;
    }
    if (type instanceof WildcardType) {
      return true;
    }
    String className = type == null ? "null" : type.getClass().getName();
    throw new IllegalArgumentException("Expected a Class, ParameterizedType, or "
        + "GenericArrayType, but <"
        + type
        + "> is of type "
        + className);
  }

  static Class<?> getRawType(Type type) {
    if (type == null) throw new NullPointerException("type == null");

    if (type instanceof Class<?>) {
      /* type is a normal class */
      return (Class<?>) type;
    }
    if (type instanceof ParameterizedType) {
      ParameterizedType parameterizedType = (ParameterizedType) type;

      /*
       * I'm not exactly sure why getRawType() returns Type instead of Class. Neal isn't either but
       * suspects some pathological case related to nested classes exists.
       */
      Type rawType = parameterizedType.getRawType();
      if (!(rawType instanceof Class)) throw new IllegalArgumentException();
      return (Class<?>) rawType;
    }
    if (type instanceof GenericArrayType) {
      Type componentType = ((GenericArrayType) type).getGenericComponentType();
      return Array.newInstance(getRawType(componentType), 0).getClass();
    }
    if (type instanceof TypeVariable) {
      /*
       * We could use the variable's bounds, but that won't work if there are multiple. Having a raw
       * type that's more general than necessary is okay.
       */
      return Object.class;
    }
    if (type instanceof WildcardType) {
      return getRawType(((WildcardType) type).getUpperBounds()[0]);
    }

    throw new IllegalArgumentException("Expected a Class, ParameterizedType, or "
        + "GenericArrayType, but <"
        + type
        + "> is of type "
        + type.getClass().getName());
  }

  /**
   * Resolve the {@link Intent} into an {@link ActivityInfo} describing the activity that should
   * execute the intent.
   *
   * @param context context to use
   * @param intent intent to resolve
   * @return {@link ActivityInfo}
   */
  static ActivityInfo resolveActivityInfo(Context context, Intent intent) {
    return intent.resolveActivityInfo(context.getPackageManager(), 0);
  }

  /**
   * Resolve a scheme url to one without parameters.
   *
   * @param schemeUrl origin scheme url
   * @return resolved scheme url
   */
  static String resolveSchemeUrl(String schemeUrl) {
    int index = schemeUrl.lastIndexOf("?");
    return index > 0 ? schemeUrl.substring(0, index) : schemeUrl;
  }
}
