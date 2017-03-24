package com.phicdy.mycuration.view.activity;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.webkit.WebView;

import com.phicdy.mycuration.R;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LicenseActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license);

        setTitle(getString(R.string.license));
        WebView webView = (WebView)findViewById(R.id.license_web_view);
        webView.loadUrl("file:///android_asset/license.html");
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}