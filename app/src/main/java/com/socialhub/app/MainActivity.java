package com.socialhub.app;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.JavascriptInterface;
import android.content.Intent;
import android.content.ActivityNotFoundException;
import android.net.Uri;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity {

private WebView webView;

private final Map<String, String> appPackages = new HashMap<>();
private final Map<String, String> webUrls = new HashMap<>();

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    initializeSocialNetworks();
    initializeWebView();
}

private void initializeSocialNetworks() {

    appPackages.put("whatsapp", "com.whatsapp");
    appPackages.put("telegram", "org.telegram.messenger");
    appPackages.put("instagram", "com.instagram.android");
    appPackages.put("facebook", "com.facebook.katana");
    appPackages.put("tiktok", "com.zhiliaoapp.musically");
    appPackages.put("youtube", "com.google.android.youtube");
    appPackages.put("x", "com.twitter.android");

    webUrls.put("whatsapp", "https://www.whatsapp.com/");
    webUrls.put("telegram", "https://web.telegram.org/");
    webUrls.put("instagram", "https://www.instagram.com/");
    webUrls.put("facebook", "https://www.facebook.com/");
    webUrls.put("tiktok", "https://www.tiktok.com/");
    webUrls.put("youtube", "https://www.youtube.com/");
    webUrls.put("x", "https://x.com/");
}

private void initializeWebView() {

    webView = new WebView(this);

    WebSettings settings = webView.getSettings();

    settings.setJavaScriptEnabled(true);
    settings.setDomStorageEnabled(true);
    settings.setAllowFileAccess(true);
    settings.setAllowContentAccess(false);
    settings.setJavaScriptCanOpenWindowsAutomatically(false);
    settings.setSupportMultipleWindows(false);

    webView.addJavascriptInterface(new AndroidBridge(), "Android");

    webView.setWebViewClient(new WebViewClient() {

        @Override
        public boolean shouldOverrideUrlLoading(
                WebView view,
                String url
        ) {

            if (url != null && url.startsWith("file:///android_asset/")) {
                return false;
            }

            if (url != null && (
                    url.startsWith("https://") ||
                    url.startsWith("http://")
            )) {
                openExternal(url);
                return true;
            }

            return true;
        }
    });

    webView.setWebChromeClient(new WebChromeClient());

    setContentView(webView);

    webView.loadUrl("file:///android_asset/index.html");
}

private void openSocial(String name) {

    if (name == null) {
        showMessage("Invalid social network");
        return;
    }

    String key = name.toLowerCase().trim();

    if (!appPackages.containsKey(key)) {
        showMessage("Social network not found");
        return;
    }

    String packageName = appPackages.get(key);
    String webUrl = webUrls.get(key);

    Intent launchIntent =
            getPackageManager().getLaunchIntentForPackage(packageName);

    if (launchIntent != null) {

        try {
            startActivity(launchIntent);
            return;

        } catch (ActivityNotFoundException e) {
            // Continue to fallback.
        }
    }

    openPlayStore(packageName, webUrl);
}

private void openPlayStore(String packageName, String webUrl) {

    try {

        Intent playIntent = new Intent(
                Intent.ACTION_VIEW,
                Uri.parse("market://details?id=" + packageName)
        );

        startActivity(playIntent);

    } catch (ActivityNotFoundException e) {

        openExternal(webUrl);
    }
}

private void openExternal(String url) {

    if (url == null) {
        return;
    }

    Uri uri = Uri.parse(url);

    if (!"https".equalsIgnoreCase(uri.getScheme())) {
        showMessage("Only secure HTTPS links are allowed");
        return;
    }

    String host = uri.getHost();

    if (host == null || !isAllowedHost(host)) {
        showMessage("This website is not allowed");
        return;
    }

    try {

        Intent intent = new Intent(
                Intent.ACTION_VIEW,
                uri
        );

        startActivity(intent);

    } catch (ActivityNotFoundException e) {

        showMessage("No browser found");
    }
}

private boolean isAllowedHost(String host) {

    String[] allowedHosts = {
            "whatsapp.com",
            "www.whatsapp.com",
            "web.telegram.org",
            "telegram.org",
            "www.telegram.org",
            "instagram.com",
            "www.instagram.com",
            "facebook.com",
            "www.facebook.com",
            "tiktok.com",
            "www.tiktok.com",
            "youtube.com",
            "www.youtube.com",
            "m.youtube.com",
            "x.com",
            "www.x.com",
            "twitter.com",
            "www.twitter.com",
            "play.google.com"
    };

    for (String allowed : allowedHosts) {
        if (host.equalsIgnoreCase(allowed)) {
            return true;
        }
    }

    return false;
}

private void showMessage(String message) {
    Toast.makeText(
            this,
            message,
            Toast.LENGTH_SHORT
    ).show();
}

private class AndroidBridge {

    @JavascriptInterface
    public void openSocial(String name) {
        runOnUiThread(() -> MainActivity.this.openSocial(name));
    }

    @JavascriptInterface
    public void openExternal(String url) {
        runOnUiThread(() -> MainActivity.this.openExternal(url));
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

@Override
protected void onDestroy() {

    if (webView != null) {
        webView.removeJavascriptInterface("Android");
        webView.destroy();
        webView = null;
    }

    super.onDestroy();
}

}
