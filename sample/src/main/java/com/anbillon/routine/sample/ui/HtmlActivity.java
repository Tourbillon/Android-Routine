package com.anbillon.routine.sample.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.anbillon.routine.sample.Navigator;
import com.anbillon.routine.sample.R;
import com.anbillon.routine.sample.SampleApplication;

/**
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
public final class HtmlActivity extends AppCompatActivity {
  private Context context;
  private Navigator navigator;

  @SuppressLint("SetJavaScriptEnabled") @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_html);

    context = this;
    navigator = ((SampleApplication) getApplication()).navigator();

    WebView webView = (WebView) findViewById(R.id.html_webview);
    webView.getSettings().setJavaScriptEnabled(true);
    webView.setWebViewClient(new HtmlClient());
    webView.loadUrl("file:///android_asset/html_scheme.html");
  }

  @SuppressWarnings("deprecation") private class HtmlClient extends WebViewClient {
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP) @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
      String host = request.getUrl().getHost();
      if (!"HTTP".equals(host) && !"HTTPS".equals(host)) {
        navigator.navigateWithDynamicSchemeUrl(context, request.getUrl().toString());
        return true;
      } else {
        return super.shouldOverrideUrlLoading(view, request);
      }
    }

    @Override public boolean shouldOverrideUrlLoading(WebView view, String url) {
      String host = Uri.parse(url).getHost();
      if (!"HTTP".equals(host) && !"HTTPS".equals(host)) {
        navigator.navigateWithDynamicSchemeUrl(context, url);
        return true;
      } else {
        return super.shouldOverrideUrlLoading(view, url);
      }
    }
  }
}
