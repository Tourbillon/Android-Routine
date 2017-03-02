package com.anbillon.routine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;

import static com.anbillon.routine.Utils.getRawType;

/**
 * A builder to build {@link Router} with methods and parameters.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
final class RouterBuilder {
  private final Intent intent;
  private String method;
  private String target;
  private String schemeUrl;
  private String pageName;
  private Class<?> page;
  private Class<?> errorPage;
  private Context context;
  private int requestCode = -1;

  public RouterBuilder(Class<?> errorPage) {
    this.errorPage = errorPage;
    this.intent = new Intent();
  }

  /**
   * Add scheme url into current call if existed.
   *
   * @param schemeUrl scheme url
   */
  void schemeUrl(String schemeUrl) {
    this.schemeUrl = schemeUrl;
    this.method = "SCHEME URL";
    this.target = schemeUrl;
  }

  /**
   * Add page target into current call if existed.
   *
   * @param pageName page name
   */
  void pageName(String pageName) {
    this.pageName = pageName;
    this.method = "PAGE NAME";
    this.target = pageName;
  }

  /**
   * Add page into current call if existed.
   *
   * @param page page
   */
  void page(Class<?> page) {
    this.page = page;
    this.method = "PAGE";
    this.target = page.getCanonicalName();
  }

  /**
   * Add flags into current call if existed.
   *
   * @param flags flags
   * @param setFlags use set method or not
   */
  void flags(int flags, boolean setFlags) {
    if (setFlags) {
      intent.setFlags(flags);
    } else {
      intent.addFlags(flags);
    }
  }

  /**
   * Add {@link Context} caller into this call.
   *
   * @param context {@link Context}
   */
  void caller(Context context) {
    this.context = context;
  }

  /**
   * Add request code into current call.
   *
   * @param requestCode request code
   */
  void requestCode(int requestCode) {
    this.requestCode = requestCode;
  }

  /**
   * Put extended data into {@link Intent}.
   *
   * @param name target of extra
   * @param type parameter type
   * @param value value to add
   * @param <T> type of @{code value}
   */
  @SuppressWarnings("unchecked") <T> void putExtra(String name, Type type, T value) {
    Class<?> rawParameterType = getRawType(type);
    if (rawParameterType == boolean.class) {
      intent.putExtra(name, Boolean.parseBoolean(value.toString()));
    } else if (rawParameterType == boolean[].class) {
      intent.putExtra(name, (boolean[]) value);
    } else if (rawParameterType == byte.class) {
      intent.putExtra(name, Byte.parseByte(value.toString()));
    } else if (rawParameterType == byte[].class) {
      intent.putExtra(name, (byte[]) value);
    } else if (rawParameterType == char.class) {
      intent.putExtra(name, value.toString().toCharArray()[0]);
    } else if (rawParameterType == char[].class) {
      intent.putExtra(name, (char[]) value);
    } else if (rawParameterType == CharSequence.class) {
      intent.putExtra(name, (CharSequence) value);
    } else if (rawParameterType == CharSequence[].class) {
      intent.putExtra(name, (CharSequence[]) value);
    } else if (rawParameterType == double.class) {
      intent.putExtra(name, Double.parseDouble(value.toString()));
    } else if (rawParameterType == double[].class) {
      intent.putExtra(name, (double[]) value);
    } else if (rawParameterType == float.class) {
      intent.putExtra(name, Float.parseFloat(value.toString()));
    } else if (rawParameterType == float[].class) {
      intent.putExtra(name, (float[]) value);
    } else if (rawParameterType == int.class) {
      intent.putExtra(name, Integer.parseInt(value.toString()));
    } else if (rawParameterType == int[].class) {
      intent.putExtra(name, (int[]) value);
    } else if (rawParameterType == long.class) {
      intent.putExtra(name, Long.parseLong(value.toString()));
    } else if (rawParameterType == long[].class) {
      intent.putExtra(name, (long[]) value);
    } else if (rawParameterType == short.class) {
      intent.putExtra(name, Short.parseShort(value.toString()));
    } else if (rawParameterType == short[].class) {
      intent.putExtra(name, (short[]) value);
    } else if (rawParameterType == String.class) {
      intent.putExtra(name, value.toString());
    } else if (rawParameterType == String[].class) {
      intent.putExtra(name, (String[]) value);
    } else if (rawParameterType == Bundle.class) {
      intent.putExtra(name, (Bundle) value);
    } else if (rawParameterType == Parcelable.class) {
      intent.putExtra(name, (Parcelable) value);
    } else if (rawParameterType == Parcelable[].class) {
      intent.putExtra(name, (Parcelable[]) value);
    } else if (rawParameterType == Serializable.class) {
      intent.putExtra(name, (Serializable) value);
    } else if (rawParameterType == Serializable[].class) {
      intent.putExtra(name, (Serializable[]) value);
    } else if (rawParameterType == ArrayList.class) {
      if (type instanceof ParameterizedType) {
        ParameterizedType parameterizedType = (ParameterizedType) type;
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        if (actualTypeArguments == null || actualTypeArguments.length != 1) {
          throw new IllegalArgumentException(
              "Unsupported type " + rawParameterType + " with target: " + name);
        }

        Type actualTypeArgument = actualTypeArguments[0];
        if (actualTypeArgument == CharSequence.class) {
          intent.putCharSequenceArrayListExtra(name, (ArrayList<CharSequence>) value);
        } else if (actualTypeArgument == String.class) {
          intent.putStringArrayListExtra(name, (ArrayList<String>) value);
        } else if (actualTypeArgument == Integer.class) {
          intent.putIntegerArrayListExtra(name, (ArrayList<Integer>) value);
        } else if (actualTypeArgument instanceof Class<?>) {
          Class<?>[] interfaces = ((Class<?>) actualTypeArgument).getInterfaces();
          for (Class<?> ininterfaceClazz : interfaces) {
            if (ininterfaceClazz == Parcelable.class) {
              intent.putParcelableArrayListExtra(name, (ArrayList<Parcelable>) value);
              return;
            }
          }

          throw new IllegalArgumentException(
              "Unsupported type " + rawParameterType + " with target: " + name);
        }
      }
    } else {
      throw new IllegalArgumentException(
          "Unsupported type " + rawParameterType + " with target: " + name);
    }
  }

  /**
   * Put extended data set into intent.
   *
   * @param extras extended data to add
   * @param <T> type of extended data
   */
  <T> void putExtras(T extras) {
    if (extras instanceof Bundle) {
      intent.putExtras((Bundle) extras);
    } else if (extras instanceof Intent) {
      intent.putExtras((Intent) extras);
    } else {
      throw new IllegalArgumentException("Unsupported type");
    }
  }

  Router build() {
    return new Router.Builder().method(method)
        .target(target)
        .schemeUrl(schemeUrl)
        .pageName(pageName)
        .page(page)
        .errorPage(errorPage)
        .context(context)
        .intent(intent)
        .requestCode(requestCode)
        .build();
  }
}
