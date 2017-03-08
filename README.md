
Routine
=======
An android router which can support scheme url and native page.


Usage
=====
* Create your own navigator:
``` java
public interface Navigator {
  String EXTRA_ID = "com.msxf.EXTRA_ID";
  /**
     * Navigate to {@link DemoActivity} with scheme url.
     *
     * @param context context to use
     */
    @SchemeUrl("demo://test/login?id=2") 
    void navigateToDemoWithSchemeUrl(@Caller Context context);

    /**
     * Navigate to {@link DemoActivity} with activity page value.
     *
     * @param context context to use
     */
    @PageName("com.msxf.module.routine.sample.ui.DemoActivity")
    @Flags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    void navigateToDemoWithPageName(@Caller Context context, @Extra(EXTRA_ID) String id,
        @RequestCode int requestCode);

    /**
     * Navigate to {@link DemoActivity} with activity page.
     *
     * @param context context to use
     */
    @Page(DemoActivity.class) 
    void navigateToDemoWithPage(@Caller Context context);
}
```
* Routine supports three navigation method: @SchemeUrl, @PageName and @Page. If you want to use dynamic shceme url, then you can create like this:
``` java
/**
  * Navigate with dynamic scheme url.
  *
  * @param context context to use
  * @param url dynamic shceme url
  */
void navigateWithDynamicSchemeUrl(@Caller Context context, @SchemeUrl String url);
```
* Then create Routine in application or somewhere:
``` java
Routine routine = new Routine.Builder().addInterceptor(new RoutineAuthInterceptor())
        .addInterceptor(
            new RoutineLoggingInterceptor().setLevel(RoutineLoggingInterceptor.Level.ALL))
        .errorPage(ErrorActivity.class)
        .build();
Navigator navigator = routine.create(Navigator.class);
```
* And then you can use navigator to navigate to the target page:
``` java
navigator.navigateToDemoWithSchemeUrl(this);
navigator.navigateToDemoWithPageName(this, "3", REQUEST_CODE);
navigator.navigateToDemoWithPage(this);
```
* If you want to navigation with @SchemeUrl, you need to add some extra properties in AndroidManifest.xml:
``` xml
<activity android:name=".ui.DemoActivity">
      <intent-filter>
        <action android:name="android.intent.action.VIEW"/>

        <category android:name="android.intent.category.DEFAULT"/>
        <category android:name="android.intent.category.BROWSABLE"/>

        <data
            android:scheme="demo"
            android:host="test"
            android:path="/login"
            />
      </intent-filter>
</activity>
```
* However, it's not safe to add scheme in AndroidManifest.xml. Maybe you only want to make navigation just inside in your app , then you  can add `Filter` to routine, one scheme url can map to many page, one page can map to many scheme urls too. The routine will open the first page which is available:
``` java
public final class SchemeFilter implements Filter {

  @Override public Matcher filter(Chain chain) {
    Matcher matcher = chain.matcher();
    Matcher.Builder builder = matcher.newBuilder();
    builder.addPage("demo://test/check", FilterActivity.class)
        .addPageName("demo://test/check", "com.msxf.module.routine.sample.ui.FilterActivity");

    return chain.proceed(builder.build());
  }
  
/* add in routine */
Routine routine = new Routine.Builder().addFilter(new SchemeFilter()).build()
```
* If you want navigate to a page with shceme url in webview of your app, then you need to replace the default method to handle scheme url with routine:
``` java
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
```
* You can also use interceptor to get data or transform Intent:
``` java
public final class RoutineAuthInterceptor implements Interceptor {

  @Override public RouterCall intercept(Chain chain) {
    RouterCall call = chain.routerCall();
    RouterCall.Builder builder = call.newBuilder();
   /* your authentication logic here */
   if (random(3) == 0) {
      Intent intent = new Intent(call.context(), LoginActivity.class);
      builder.intent(intent).requestCode(-1);
    }

    return chain.proceed(builder.build());
  }
}
```
* Enjoy it.



Download
========

	compile 'com.anbillon.routine:routine:latest-version'



Credits
=======
* [Retrofit][1] - Type-safe HTTP client for Android and Java by Square, Inc



License
=======

    Copyright (C) 2017 Tourbillon Group

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
         http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.


[1]: https://github.com/square/retrofit