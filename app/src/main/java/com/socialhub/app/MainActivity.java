package com.socialhub.app;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings;
import android.webkit.JavascriptInterface;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity {

    private WebView webView;

    private final Map<String, String> socialApps = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        socialApps.put("whatsapp", "com.whatsapp");
        socialApps.put("telegram", "org.telegram.messenger");
        socialApps.put("instagram", "com.instagram.android");
        socialApps.put("facebook", "com.facebook.katana");
        socialApps.put("tiktok", "com.zhiliaoapp.musically");
        socialApps.put("youtube", "com.google.android.youtube");
        socialApps.put("x", "com.twitter.android");

        webView = new WebView(this);

        WebSettings settings = webView.getSettings();

        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccess(true);

        webView.setWebViewClient(new WebViewClient());

        webView.addJavascriptInterface(
            new AndroidBridge(),
            "Android"
        );

        setContentView(webView);

        webView.loadUrl(
            "file:///android_asset/index.html"
        );
    }

    public class AndroidBridge {

        @JavascriptInterface
        public void openSocial(String name) {

            runOnUiThread(() -> {

                String packageName = socialApps.get(name);

                if (packageName == null) {
                    Toast.makeText(
                        MainActivity.this,
                        "برنامه شناخته نشد",
                        Toast.LENGTH_SHORT
                    ).show();
                    return;
                }

                try {

                    Intent launchIntent =
                        getPackageManager()
                        .getLaunchIntentForPackage(packageName);

                    if (launchIntent != null) {

                        startActivity(launchIntent);

                    } else {

                        openPlayStore(packageName);

                    }

                } catch (Exception e) {

                    openPlayStore(packageName);

                }

            });
        }

        private void openPlayStore(String packageName) {

            try {

                Intent playIntent = new Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + packageName)
                );

                startActivity(playIntent);

            } catch (Exception e) {

                try {

                    Intent browserIntent = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(
                            "https://play.google.com/store/apps/details?id="
                            + packageName
                        )
                    );

                    startActivity(browserIntent);

                } catch (Exception ex) {

                    Toast.makeText(
                        MainActivity.this,
                        "امکان بازکردن فروشگاه وجود ندارد",
                        Toast.LENGTH_LONG
                    ).show();

                }
            }
        }
    }

    @Override
    public void onBackPressed() {

        if (webView != null && webView.canGoBack()) {

            webView.goBack();

        } else {

            super.onBackPressed();

        }
    }
}
