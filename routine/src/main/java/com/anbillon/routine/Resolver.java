package com.anbillon.routine;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;

/**
 * A context to resolve different caller.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
public interface Resolver {
  String callerName();

  Context context();

  void startActivity(Intent intent) throws ActivityNotFoundException;

  void startActivityForResult(Intent intent, int requestCode) throws ActivityNotFoundException;

  abstract class Factory {

    public <T> Resolver create(T caller) {
      return null;
    }
  }
}
