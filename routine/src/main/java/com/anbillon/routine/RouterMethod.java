package com.anbillon.routine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.anbillon.routine.app.Caller;
import com.anbillon.routine.app.Extra;
import com.anbillon.routine.app.ExtraSet;
import com.anbillon.routine.app.Flags;
import com.anbillon.routine.app.Page;
import com.anbillon.routine.app.PageName;
import com.anbillon.routine.app.RequestCode;
import com.anbillon.routine.app.SchemeUrl;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * Parse an invocation of an interface method into a router.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
final class RouterMethod {
  private final MethodHandler<?>[] methodHandlers;
  private final ParameterHandler<?>[] parameterHandlers;
  private final Class<?> errorPage;
  private final Type returnType;

  RouterMethod(Builder builder) {
    this.methodHandlers = builder.methodHandlers;
    this.parameterHandlers = builder.parameterHandlers;
    this.errorPage = builder.routine.errorPage();
    this.returnType = builder.method.getGenericReturnType();
  }

  Router toRouter(Object... args) throws IllegalArgumentException {
    RouterBuilder routerBuilder = new RouterBuilder(errorPage);

    for (MethodHandler<?> handler : methodHandlers) {
      handler.apply(routerBuilder);
    }

    /* It is an error to invoke a method with the wrong arg types. */
    @SuppressWarnings("unchecked") ParameterHandler<Object>[] handlers =
        (ParameterHandler<Object>[]) parameterHandlers;
    int argumentCount = args != null ? args.length : 0;
    if (argumentCount != handlers.length) {
      throw new IllegalArgumentException("Argument count ("
          + argumentCount
          + ") doesn't match expected count ("
          + handlers.length
          + ")");
    }

    for (int p = 0; p < argumentCount; p++) {
      handlers[p].apply(routerBuilder, args[p]);
    }

    return routerBuilder.build();
  }

  Type returnType() {
    return returnType;
  }

  static final class Builder {
    final Routine routine;
    final Method method;
    final Annotation[] methodAnnotations;
    final Annotation[][] parameterAnnotationsArray;
    final Type[] parameterTypes;

    boolean gotCaller;
    boolean gotRequestCode;
    boolean gotSchemeUrl;
    boolean gotExtraSet;
    ParameterHandler<?>[] parameterHandlers;
    MethodHandler<?>[] methodHandlers;

    public Builder(Routine routine, Method method) {
      this.routine = routine;
      this.method = method;
      this.methodAnnotations = method.getAnnotations();
      this.parameterTypes = method.getGenericParameterTypes();
      this.parameterAnnotationsArray = method.getParameterAnnotations();
    }

    public RouterMethod build() {
      int methodCount = methodAnnotations.length;
      methodHandlers = new MethodHandler<?>[methodCount];
      for (int m = 0; m < methodCount; m++) {
        methodHandlers[m] = parseMethodAnnotation(methodAnnotations[m]);
      }

      int parameterCount = parameterAnnotationsArray.length;
      parameterHandlers = new ParameterHandler<?>[parameterCount];
      for (int p = 0; p < parameterCount; p++) {
        Type parameterType = parameterTypes[p];
        if (Utils.hasUnresolvableType(parameterType)) {
          throw parameterError(p, "Parameter type must not include a type variable or wildcard: %s",
              parameterType);
        }

        Annotation[] parameterAnnotations = parameterAnnotationsArray[p];
        if (parameterAnnotations == null) {
          throw parameterError(p, "No Routine annotation found.");
        }

        parameterHandlers[p] = parseParameter(p, parameterType, parameterAnnotations);
      }

      if (methodHandlers.length == 0 && !gotSchemeUrl) {
        throw methodError(
            "Routine method annotation is required (@SchemeUrl, @Page or @PageName).");
      }

      if (!gotCaller) {
        throw methodError("A call must contain one @Caller.");
      }

      return new RouterMethod(this);
    }

    private MethodHandler<?> parseMethodAnnotation(Annotation annotation) {
      if (annotation instanceof SchemeUrl) {
        gotSchemeUrl = true;
        return new MethodHandler.SchemeUrl(((SchemeUrl) annotation).value());
      } else if (annotation instanceof PageName) {
        return new MethodHandler.PageName(((PageName) annotation).value());
      } else if (annotation instanceof Page) {
        return new MethodHandler.Page(((Page) annotation).value());
      } else if (annotation instanceof Flags) {
        Flags flags = (Flags) annotation;
        return new MethodHandler.Flags(flags.value(), (flags.set()));
      }

      /* no annotation method */
      throw methodError("No routine method annotation found", annotation);
    }

    private ParameterHandler<?> parseParameterAnnotation(int p, Type type, Annotation annotation) {
      if (annotation instanceof Caller) {
        if (gotCaller) {
          throw parameterError(p, "Multiple @Caller parameter annotations found.");
        }

        gotCaller = true;

        if (type == Context.class) {
          return new ParameterHandler.Caller();
        } else {
          throw parameterError(p, "@Caller must be android.content.Context type.");
        }
      }

      if (annotation instanceof RequestCode) {
        if (gotRequestCode) {
          throw parameterError(p, "Multiple @RequestCode parameter annotations found.");
        }

        gotRequestCode = true;

        if (type == int.class) {
          return new ParameterHandler.RequestCode();
        } else {
          throw parameterError(p, "@RequestCode must be int type.");
        }
      }

      if (annotation instanceof SchemeUrl) {
        if (gotSchemeUrl) {
          throw parameterError(p, "Multiple @SchemeUrl annotations found on method and parameter.");
        }

        gotSchemeUrl = true;

        return new ParameterHandler.SchemeUrl();
      }

      if (annotation instanceof Extra) {
        String name = ((Extra) annotation).value();
        return new ParameterHandler.Extra<>(name, type);
      }

      if (annotation instanceof ExtraSet) {
        if (gotExtraSet) {
          throw parameterError(p, "Multiple @ExtraSet parameter annotations found.");
        }

        gotExtraSet = true;

        if (type == Bundle.class || type == Intent.class) {
          return new ParameterHandler.ExtraSet<>();
        } else {
          throw parameterError(p,
              "@ExtraSet must be android.os.Bundle or android.content.Intent type.");
        }
      }

      /* no annotation paramater */
      return null;
    }

    private ParameterHandler<?> parseParameter(int p, Type parameterType,
        Annotation[] annotations) {
      ParameterHandler<?> result = null;
      for (Annotation annotation : annotations) {
        ParameterHandler<?> annotationAction =
            parseParameterAnnotation(p, parameterType, annotation);
        if (annotationAction == null) {
          continue;
        }

        if (result != null) {
          throw parameterError(p, "Multiple Routine annotations found, only one allowed.");
        }

        result = annotationAction;
      }

      if (result == null) {
        throw parameterError(p, "No Routine annotation found.");
      }

      return result;
    }

    private RuntimeException methodError(String message, Object... args) {
      return methodError(null, message, args);
    }

    private RuntimeException methodError(Throwable cause, String message, Object... args) {
      message = String.format(message, args);
      return new IllegalArgumentException(message
          + "\n for method "
          + method.getDeclaringClass().getSimpleName()
          + "."
          + method.getName(), cause);
    }

    private RuntimeException parameterError(int p, String message, Object... args) {
      return methodError(message + " (parameter #" + (p + 1) + ")", args);
    }
  }
}
