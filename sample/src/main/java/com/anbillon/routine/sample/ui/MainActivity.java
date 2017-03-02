package com.anbillon.routine.sample.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.anbillon.routine.sample.Navigator;
import com.anbillon.routine.sample.R;
import com.anbillon.routine.sample.SampleApplication;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
  private static final int REQUEST_CODE = 0x1209;
  private Navigator navigator;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    findViewById(R.id.btn_scheme_url).setOnClickListener(this);
    findViewById(R.id.btn_page_name).setOnClickListener(this);
    findViewById(R.id.btn_page).setOnClickListener(this);
    findViewById(R.id.btn_html_scheme).setOnClickListener(this);
    findViewById(R.id.btn_not_found).setOnClickListener(this);

    navigator = ((SampleApplication) getApplication()).navigator();
  }

  @Override public void onClick(View v) {
    int id = v.getId();
    switch (id) {
      case R.id.btn_scheme_url:
        navigator.navigateToDemoWithSchemeUrl(this);
        break;

      case R.id.btn_page_name:
        navigator.navigateToDemoWithPageName(this, "3", REQUEST_CODE);
        break;

      case R.id.btn_page:
        navigator.navigateToDemoWithPage(this);
        break;

      case R.id.btn_html_scheme:
        navigator.navigateToHtml(this);
        break;

      case R.id.btn_not_found:
        navigator.navigateWithNotFound(this);
        break;
    }
  }
}
