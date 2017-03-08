package com.anbillon.routine.support;

import android.support.v4.app.Fragment;
import com.anbillon.routine.Resolver;

/**
 * A {@link Resolver.Factory} for support fragment.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
public final class SupportFragmentResolverFactory extends Resolver.Factory {

  private SupportFragmentResolverFactory() {
  }

  public static SupportFragmentResolverFactory create() {
    return new SupportFragmentResolverFactory();
  }

  @Override public <T> Resolver create(T caller) {
    if (caller instanceof Fragment) {
      return new SupportFragmentResolver((Fragment) caller);
    }

    return super.create(caller);
  }
}
