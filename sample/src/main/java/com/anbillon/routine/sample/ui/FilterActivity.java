package com.anbillon.routine.sample.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import com.anbillon.routine.sample.R;

/**
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
public final class FilterActivity extends AppCompatActivity {

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sample);

    TextView textView = (TextView) findViewById(R.id.sample_tv);
    textView.setText("Filter page");
  }
}
