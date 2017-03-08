package com.anbillon.routine.support;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import com.anbillon.routine.Resolver;

/**
 * A resolver for support fragment.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
final class SupportFragmentResolver implements Resolver {
  private final Fragment fragment;

  SupportFragmentResolver(Fragment fragment) {
    this.fragment = fragment;
  }

  @Override public String callerName() {
    return fragment.getClass().getCanonicalName();
  }

  @Override public Context context() {
    return fragment.getContext();
  }

  @Override public void startActivity(Intent intent) throws ActivityNotFoundException {
    fragment.startActivity(intent);
  }

  @Override public void startActivityForResult(Intent intent, int requestCode)
      throws ActivityNotFoundException {
    fragment.startActivityForResult(intent, requestCode);
  }
}
