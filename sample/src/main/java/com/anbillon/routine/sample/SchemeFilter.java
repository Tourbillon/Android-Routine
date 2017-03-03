package com.anbillon.routine.sample;

import com.anbillon.routine.Filter;
import com.anbillon.routine.Matcher;
import com.anbillon.routine.sample.ui.FilterActivity;

/**
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
public final class SchemeFilter implements Filter {

  @Override public Matcher filter(Chain chain) {
    Matcher matcher = chain.matcher();
    Matcher.Builder builder = matcher.newBuilder();
    builder.addPage("demo://test/check", FilterActivity.class)
        .addPageName("demo://test/check", "com.msxf.module.routine.sample.ui.FilterActivity");

    return chain.proceed(builder.build());
  }
}
