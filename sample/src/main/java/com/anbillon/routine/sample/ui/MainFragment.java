package com.anbillon.routine.sample.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.anbillon.routine.sample.Navigator;
import com.anbillon.routine.sample.R;
import com.anbillon.routine.sample.SampleApplication;

/**
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
public final class MainFragment extends Fragment implements View.OnClickListener {
  private static final int REQUEST_CODE = 0x1209;
  private Navigator navigator;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_main, container, false);
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    view.findViewById(R.id.btn_scheme_url).setOnClickListener(this);
    view.findViewById(R.id.btn_page_name).setOnClickListener(this);
    view.findViewById(R.id.btn_page).setOnClickListener(this);
    view.findViewById(R.id.btn_scheme_filter).setOnClickListener(this);
    view.findViewById(R.id.btn_html_scheme).setOnClickListener(this);
    view.findViewById(R.id.btn_not_found).setOnClickListener(this);
    navigator = ((SampleApplication) getActivity().getApplication()).navigator();
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    Log.d("TAG", "requestCode: " + requestCode);
  }

  @Override public void onClick(View v) {
    int id = v.getId();
    switch (id) {
      case R.id.btn_scheme_url:
        navigator.navigateToDemoWithSchemeUrl(getActivity());
        break;

      case R.id.btn_page_name:
        navigator.navigateToDemoWithPageName(getActivity(), "3", REQUEST_CODE);
        break;

      case R.id.btn_page:
        navigator.navigateToDemoWithPage(this);
        break;

      case R.id.btn_scheme_filter:
        navigator.navigateWithFilters(getActivity());
        break;

      case R.id.btn_html_scheme:
        navigator.navigateToHtml(getActivity());
        break;

      case R.id.btn_not_found:
        navigator.navigateWithNotFound(getActivity());
        break;
    }
  }
}
