package com.anbillon.routine.sample;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import com.anbillon.routine.app.Caller;
import com.anbillon.routine.app.Extra;
import com.anbillon.routine.app.Page;
import com.anbillon.routine.app.SchemeUrl;
import com.anbillon.routine.sample.ui.DemoActivity;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
public interface ExtraNavigator {
  String EXTRA = "com.msxf.EXTRA";

  @Page(DemoActivity.class) void withBoolean(@Caller Context context, @Extra(EXTRA) boolean extra);

  @Page(DemoActivity.class) void withBooleanArray(@Caller Context context,
      @Extra(EXTRA) boolean[] extra);

  @Page(DemoActivity.class) void withByte(@Caller Context context, @Extra(EXTRA) byte extra);

  @Page(DemoActivity.class) void withByteArray(@Caller Context context, @Extra(EXTRA) byte[] extra);

  @Page(DemoActivity.class) void withChar(@Caller Context context, @Extra(EXTRA) char extra);

  @Page(DemoActivity.class) void withCharArray(@Caller Context context, @Extra(EXTRA) char[] extra);

  @Page(DemoActivity.class) void withCharSequence(@Caller Context context,
      @Extra(EXTRA) CharSequence extra);

  @Page(DemoActivity.class) void withCharSequenceArray(@Caller Context context,
      @Extra(EXTRA) CharSequence[] extra);

  @Page(DemoActivity.class) void withDouble(@Caller Context context, @Extra(EXTRA) double extra);

  @Page(DemoActivity.class) void withDoubleArray(@Caller Context context,
      @Extra(EXTRA) double[] extra);

  @Page(DemoActivity.class) void withFloat(@Caller Context context, @Extra(EXTRA) float extra);

  @Page(DemoActivity.class) void withFloatArray(@Caller Context context,
      @Extra(EXTRA) float[] extra);

  @Page(DemoActivity.class) void withInt(@Caller Context context, @Extra(EXTRA) int extra);

  @Page(DemoActivity.class) void withIntArray(@Caller Context context, @Extra(EXTRA) int[] extra);

  @Page(DemoActivity.class) void withLong(@Caller Context context, @Extra(EXTRA) long extra);

  @Page(DemoActivity.class) void withLongArray(@Caller Context context, @Extra(EXTRA) long[] extra);

  @Page(DemoActivity.class) void withShort(@Caller Context context, @Extra(EXTRA) short extra);

  @Page(DemoActivity.class) void withShortArray(@Caller Context context,
      @Extra(EXTRA) short[] extra);

  @Page(DemoActivity.class) void withString(@Caller Context context, @Extra(EXTRA) String extra);

  @Page(DemoActivity.class) void withStringArray(@Caller Context context,
      @Extra(EXTRA) String[] extra);

  @Page(DemoActivity.class) void withBundle(@Caller Context context, @Extra(EXTRA) Bundle extra);

  @Page(DemoActivity.class) void withParcelable(@Caller Context context,
      @Extra(EXTRA) Parcelable extra);

  @Page(DemoActivity.class) void withParcelableArray(@Caller Context context,
      @Extra(EXTRA) Parcelable[] extra);

  @Page(DemoActivity.class) void withSerializable(@Caller Context context,
      @Extra(EXTRA) Serializable extra);

  @Page(DemoActivity.class) void withSerializableArray(@Caller Context context,
      @Extra(EXTRA) Serializable[] extra);

  @Page(DemoActivity.class) void withCharSequenceList(@Caller Context context,
      @Extra(EXTRA) ArrayList<CharSequence> extra);

  @Page(DemoActivity.class) void withStringList(@Caller Context context,
      @Extra(EXTRA) ArrayList<String> extra);

  @Page(DemoActivity.class) void withIntList(@Caller Context context,
      @Extra(EXTRA) ArrayList<Integer> extra);

  @Page(DemoActivity.class) void withParcelableList(@Caller Context context,
      @Extra(EXTRA) ArrayList<ParcelableExtra> extra);

  @SchemeUrl("demo://test/login?id=2") void withSchemeUrl(@Caller Context context);
}
