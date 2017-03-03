package com.anbillon.routine.sample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.anbillon.routine.Routine;
import com.anbillon.routine.logging.RoutineLoggingInterceptor;
import com.anbillon.routine.sample.ui.ErrorActivity;
import com.anbillon.routine.sample.ui.MainActivity;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;
import org.robolectric.shadows.ShadowLog;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.robolectric.Shadows.shadowOf;

/**
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
@RunWith(RobolectricTestRunner.class) @Config(constants = BuildConfig.class, sdk = 21)
public final class ExtrasTest {
  private Context context;
  private ExtraNavigator navigator;
  private MainActivity mainActivity;

  @Before public void setUp() {
    ShadowLog.stream = System.out;
    context = ShadowApplication.getInstance().getApplicationContext();
    mainActivity = Robolectric.setupActivity(MainActivity.class);
    Routine routine = new Routine.Builder().addInterceptor(
        new RoutineLoggingInterceptor().setLevel(RoutineLoggingInterceptor.Level.ALL))
        .errorPage(ErrorActivity.class)
        .build();
    navigator = routine.create(ExtraNavigator.class);
  }

  public Intent nextIntent() {
    return shadowOf(mainActivity).getNextStartedActivity();
  }

  @Test public void testBooleanExtra() throws Exception {
    navigator.withBoolean(context, true);
    boolean result = nextIntent().getBooleanExtra(ExtraNavigator.EXTRA, false);
    assertEquals(true, result);
  }

  @Test public void testBooleanArrayExtra() throws Exception {
    navigator.withBooleanArray(context, new boolean[] { true, true, false });
    boolean[] result = nextIntent().getBooleanArrayExtra(ExtraNavigator.EXTRA);
    assertEquals(3, result.length);
    assertEquals(true, result[0]);
    assertEquals(true, result[1]);
    assertEquals(false, result[2]);
  }

  @Test public void testByteExtra() throws Exception {
    navigator.withByte(context, Byte.valueOf("11"));
    byte result = nextIntent().getByteExtra(ExtraNavigator.EXTRA, Byte.valueOf("0"));
    assertEquals(Byte.valueOf("11").toString(), Byte.toString(result));
  }

  @Test public void testByteArrayExtra() throws Exception {
    byte[] extras = { 1, 2, 3 };
    navigator.withByteArray(context, extras);
    byte[] result = nextIntent().getByteArrayExtra(ExtraNavigator.EXTRA);
    assertNotNull(result);
    assertEquals(3, result.length);
    assertEquals(1, result[0]);
    assertEquals(2, result[1]);
    assertEquals(3, result[2]);
  }

  @Test public void testCharExtra() throws Exception {
    navigator.withChar(context, 'x');
    char result = nextIntent().getCharExtra(ExtraNavigator.EXTRA, 'y');
    assertEquals('x', result);
  }

  @Test public void testCharArrayExtra() throws Exception {
    navigator.withCharArray(context, new char[] { 'w', 'x', 'y', 'z' });
    char[] result = nextIntent().getCharArrayExtra(ExtraNavigator.EXTRA);
    assertNotNull(result);
    assertEquals(4, result.length);
    assertEquals('w', result[0]);
    assertEquals('x', result[1]);
    assertEquals('y', result[2]);
    assertEquals('z', result[3]);
  }

  @Test public void testCharSequenceExtra() throws Exception {
    navigator.withCharSequence(context, "x");
    CharSequence result = nextIntent().getCharSequenceExtra(ExtraNavigator.EXTRA);
    assertEquals("x", result);
  }

  @Test public void testCharSequenceArrayExtra() throws Exception {
    navigator.withCharSequenceArray(context, new CharSequence[] { "www", "xxxx", "yyy", "zzz" });
    CharSequence[] result = nextIntent().getCharSequenceArrayExtra(ExtraNavigator.EXTRA);
    assertNotNull(result);
    assertEquals(4, result.length);
    assertEquals("www", result[0]);
    assertEquals("xxxx", result[1]);
    assertEquals("yyy", result[2]);
    assertEquals("zzz", result[3]);
  }

  @Test public void testDoubleExtra() throws Exception {
    navigator.withDouble(context, 2.33546);
    double result = nextIntent().getDoubleExtra(ExtraNavigator.EXTRA, 0);
    assertEquals(2.33546, result, 0);
  }

  @Test public void testDoubleArrayExtra() throws Exception {
    navigator.withDoubleArray(context, new double[] { 2.33546, 3.490894689, 5.70998437, 8.8876 });
    double[] result = nextIntent().getDoubleArrayExtra(ExtraNavigator.EXTRA);
    assertNotNull(result);
    assertEquals(4, result.length);
    assertEquals(2.33546, result[0], 0);
    assertEquals(3.490894689, result[1], 0);
    assertEquals(5.70998437, result[2], 0);
    assertEquals(8.8876, result[3], 0);
  }

  @Test public void testFloatExtra() throws Exception {
    navigator.withFloat(context, 2.33546f);
    float result = nextIntent().getFloatExtra(ExtraNavigator.EXTRA, 0);
    assertEquals(2.33546f, result, 0);
  }

  @Test public void testFloatArrayExtra() throws Exception {
    navigator.withFloatArray(context, new float[] { 2.33546f, 3.490894689f, 5.70998437f, 8.8876f });
    float[] result = nextIntent().getFloatArrayExtra(ExtraNavigator.EXTRA);
    assertNotNull(result);
    assertEquals(4, result.length);
    assertEquals(2.33546f, result[0], 0);
    assertEquals(3.490894689f, result[1], 0);
    assertEquals(5.70998437f, result[2], 0);
    assertEquals(8.8876f, result[3], 0);
  }

  @Test public void testIntExtra() throws Exception {
    navigator.withInt(context, 12);
    int result = nextIntent().getIntExtra(ExtraNavigator.EXTRA, 0);
    assertEquals(12, result);
  }

  @Test public void testIntArrayExtra() throws Exception {
    navigator.withIntArray(context, new int[] { 12, 23, 34, 45 });
    int[] result = nextIntent().getIntArrayExtra(ExtraNavigator.EXTRA);
    assertNotNull(result);
    assertEquals(4, result.length);
    assertEquals(12, result[0], 0);
    assertEquals(23, result[1], 0);
    assertEquals(34, result[2], 0);
    assertEquals(45, result[3], 0);
  }

  @Test public void testLongExtra() throws Exception {
    navigator.withLong(context, 1212L);
    long result = nextIntent().getLongExtra(ExtraNavigator.EXTRA, 0);
    assertEquals(1212, result);
  }

  @Test public void testLongArrayExtra() throws Exception {
    navigator.withLongArray(context, new long[] { 1212L, 2323L, 3434L, 4545L });
    long[] result = nextIntent().getLongArrayExtra(ExtraNavigator.EXTRA);
    assertNotNull(result);
    assertEquals(4, result.length);
    assertEquals(1212L, result[0], 0);
    assertEquals(2323L, result[1], 0);
    assertEquals(3434L, result[2], 0);
    assertEquals(4545L, result[3], 0);
  }

  @Test public void testShortExtra() throws Exception {
    navigator.withShort(context, Short.valueOf("23"));
    short result = nextIntent().getShortExtra(ExtraNavigator.EXTRA, Short.valueOf("0"));
    assertEquals(23, result);
  }

  @Test public void testShortArrayExtra() throws Exception {
    navigator.withShortArray(context, new short[] { 12, 23, 34, 45 });
    short[] result = nextIntent().getShortArrayExtra(ExtraNavigator.EXTRA);
    assertNotNull(result);
    assertEquals(4, result.length);
    assertEquals(12, result[0]);
    assertEquals(23, result[1]);
    assertEquals(34, result[2]);
    assertEquals(45, result[3]);
  }

  @Test public void testStringExtra() throws Exception {
    navigator.withString(context, "This is string");
    String result = nextIntent().getStringExtra(ExtraNavigator.EXTRA);
    assertEquals("This is string", result);
  }

  @Test public void testStringArrayExtra() throws Exception {
    navigator.withStringArray(context, new String[] {
        "This is string", "This string", "This is not string", "Do you know"
    });
    String[] result = nextIntent().getStringArrayExtra(ExtraNavigator.EXTRA);
    assertNotNull(result);
    assertEquals(4, result.length);
    assertEquals("This is string", result[0]);
    assertEquals("This string", result[1]);
    assertEquals("This is not string", result[2]);
    assertEquals("Do you know", result[3]);
  }

  @Test public void testBundleExtra() throws Exception {
    Bundle bundle = new Bundle();
    bundle.putInt("bundle_int", 1);
    navigator.withBundle(context, bundle);
    Bundle result = nextIntent().getBundleExtra(ExtraNavigator.EXTRA);
    assertNotNull(result);
    assertEquals(1, result.getInt("bundle_int"));
  }

  @Test public void testParcelableExtra() throws Exception {
    ParcelableExtra extra = new ParcelableExtra("Vincent");
    navigator.withParcelable(context, extra);
    ParcelableExtra result = nextIntent().getParcelableExtra(ExtraNavigator.EXTRA);
    assertNotNull(result);
    assertEquals("Vincent", result.name);
  }

  @Test public void testParcelableArrayExtra() throws Exception {
    ParcelableExtra[] extras =
        new ParcelableExtra[] { new ParcelableExtra("Vincent"), new ParcelableExtra("Cously") };
    navigator.withParcelableArray(context, extras);
    ParcelableExtra[] result =
        (ParcelableExtra[]) nextIntent().getParcelableArrayExtra(ExtraNavigator.EXTRA);
    assertNotNull(result);
    assertEquals("Vincent", result[0].name);
    assertEquals("Cously", result[1].name);
  }

  @Test public void testSerializableExtra() throws Exception {
    SerializableExtra extra = new SerializableExtra("123");
    navigator.withSerializable(context, extra);
    SerializableExtra result =
        (SerializableExtra) nextIntent().getSerializableExtra(ExtraNavigator.EXTRA);
    assertNotNull(result);
    assertEquals("123", result.id);
  }

  @Test public void testSerializableArrayExtra() throws Exception {
    SerializableExtra[] extras =
        new SerializableExtra[] { new SerializableExtra("123"), new SerializableExtra("456") };
    navigator.withSerializableArray(context, extras);
    SerializableExtra[] result =
        (SerializableExtra[]) nextIntent().getSerializableExtra(ExtraNavigator.EXTRA);
    assertNotNull(result);
    assertEquals("123", result[0].id);
    assertEquals("456", result[1].id);
  }

  @Test public void testCharSequenceListExtra() throws Exception {
    ArrayList<CharSequence> extra = new ArrayList<>();
    extra.add("123");
    extra.add("456");
    navigator.withCharSequenceList(context, extra);
    ArrayList<CharSequence> result =
        nextIntent().getCharSequenceArrayListExtra(ExtraNavigator.EXTRA);
    assertNotNull(result);
    assertEquals("123", result.get(0));
    assertEquals("456", result.get(1));
  }

  @Test public void testStringListExtra() throws Exception {
    ArrayList<String> extra = new ArrayList<>();
    extra.add("123");
    extra.add("456");
    navigator.withStringList(context, extra);
    ArrayList<String> result = nextIntent().getStringArrayListExtra(ExtraNavigator.EXTRA);
    assertNotNull(result);
    assertEquals("123", result.get(0));
    assertEquals("456", result.get(1));
  }

  @Test public void testIntListExtra() throws Exception {
    ArrayList<Integer> extra = new ArrayList<>();
    extra.add(123);
    extra.add(456);
    navigator.withIntList(context, extra);
    ArrayList<Integer> result = nextIntent().getIntegerArrayListExtra(ExtraNavigator.EXTRA);
    assertNotNull(result);
    assertEquals(123, result.get(0).intValue());
    assertEquals(456, result.get(1).intValue());
  }

  @Test public void testParcelableListExtra() throws Exception {
    ArrayList<ParcelableExtra> extra = new ArrayList<>();
    extra.add(new ParcelableExtra("Vincent"));
    extra.add(new ParcelableExtra("Cously"));
    navigator.withParcelableList(context, extra);
    ArrayList<ParcelableExtra> result =
        nextIntent().getParcelableArrayListExtra(ExtraNavigator.EXTRA);
    assertNotNull(result);
    assertEquals("Vincent", result.get(0).name);
    assertEquals("Cously", result.get(1).name);
  }

  @Test public void testSchemeUrlExtra() throws Exception {
    navigator.withSchemeUrl(context);
    String id = nextIntent().getStringExtra("id");
    assertEquals("2", id);
  }
}
