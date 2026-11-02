package com.socialhub.app;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.JavascriptInterface;
import android.content.Intent;
import android.content.ActivityNotFoundException;
import android.net.Uri;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity {

    private WebView webView;
    private final Handler handler = new Handler();

    private final Map<String, String> appPackages = new HashMap<>();
    private final Map<String, String> webUrls = new HashMap<>();

    private final int BG = Color.rgb(5, 0, 22);
    private final int PURPLE = Color.rgb(170, 55, 255);
    private final int PINK = Color.rgb(220, 75, 255);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeSocialNetworks();
        showSplashScreen();
    }

    private GradientDrawable createBackground(
            int color, int stroke, int radius) {

        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(color);
        drawable.setCornerRadius(radius);

        if (stroke != Color.TRANSPARENT) {
            drawable.setStroke(3, stroke);
        }

        return drawable;
    }

    private TextView createText(
            String text,
            int size,
            int color,
            boolean bold) {

        TextView view = new TextView(this);

        view.setText(text);
        view.setTextSize(size);
        view.setTextColor(color);
        view.setGravity(Gravity.CENTER);

        view.setTypeface(
                Typeface.create(
                        "sans-serif",
                        bold ? Typeface.BOLD : Typeface.NORMAL
                )
        );

        return view;
    }

    // ================= SPLASH SCREEN =================

    private void showSplashScreen() {

        FrameLayout root = new FrameLayout(this);

        GradientDrawable background = new GradientDrawable(
                GradientDrawable.Orientation.TL_BR,
                new int[]{
                        Color.rgb(3, 0, 18),
                        Color.rgb(18, 5, 55),
                        Color.rgb(4, 0, 25)
                }
        );

        root.setBackground(background);

        LinearLayout content = new LinearLayout(this);

        content.setOrientation(LinearLayout.VERTICAL);
        content.setGravity(Gravity.CENTER);
        content.setPadding(25, 30, 25, 30);

        // MRM LOGO

        TextView logo = createText(
                "MRM",
                46,
                Color.WHITE,
                true
        );

        logo.setBackground(
                createBackground(
                        Color.rgb(13, 8, 55),
                        PURPLE,
                        100
                )
        );

        LinearLayout.LayoutParams logoParams =
                new LinearLayout.LayoutParams(190, 190);

        logoParams.bottomMargin = 12;

        content.addView(logo, logoParams);

        // LOGO SUBTITLE

        TextView logoSubtitle = createText(
                "SOCIAL HUB",
                18,
                Color.WHITE,
                true
        );

        content.addView(logoSubtitle);

        // APPLICATION NAME

        TextView title = createText(
                "MRM Social Hub",
                29,
                PINK,
                true
        );

        LinearLayout.LayoutParams titleParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        titleParams.topMargin = 28;

        content.addView(title, titleParams);

        // SLOGAN

        TextView slogan = createText(
                "Connect  •  Share  •  Explore",
                15,
                Color.WHITE,
                false
        );

        LinearLayout.LayoutParams sloganParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        sloganParams.topMargin = 12;

        content.addView(slogan, sloganParams);

        // SOCIAL NETWORKS

        TextView networks = createText(
                "𝕏     ●     ✈     f     ♪     ◎     ▶",
                25,
                Color.rgb(190, 125, 255),
                true
        );

        LinearLayout.LayoutParams networkParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        networkParams.topMargin = 45;

        content.addView(networks, networkParams);

        // POWERED BY

        TextView powered = createText(
                "POWERED BY",
                11,
                Color.LTGRAY,
                false
        );

        LinearLayout.LayoutParams poweredParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        poweredParams.topMargin = 55;

        content.addView(powered, poweredParams);

        TextView company = createText(
                "MRM TECHNOLOGY TEAM",
                13,
                PINK,
                true
        );

        content.addView(company);

        root.addView(
                content,
                new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT
                )
        );

        setContentView(root);

        handler.postDelayed(
                this::initializeWebView,
                2000
        );
    }

    // ================= SOCIAL NETWORKS =================

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

    // ================= WEBVIEW =================

    private void initializeWebView() {

        webView = new WebView(this);

        WebSettings settings = webView.getSettings();

        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(false);

        settings.setJavaScriptCanOpenWindowsAutomatically(false);
        settings.setSupportMultipleWindows(false);

        webView.addJavascriptInterface(
                new AndroidBridge(),
                "Android"
        );

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(
                    WebView view,
                    String url) {

                if (url != null &&
                        url.startsWith("file:///android_asset/")) {
                    return false;
                }

                if (url != null &&
                        (url.startsWith("https://") ||
                         url.startsWith("http://"))) {

                    openExternal(url);
                    return true;
                }

                return true;
            }
        });

        webView.setWebChromeClient(new WebChromeClient());

        setContentView(webView);

        webView.loadUrl(
                "file:///android_asset/index.html"
        );
    }

    // ================= OPEN SOCIAL NETWORK =================

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
                getPackageManager().getLaunchIntentForPackage(
                        packageName
                );

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

    private void openPlayStore(
            String packageName,
            String webUrl) {

        try {

            Intent intent = new Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(
                            "market://details?id=" + packageName
                    )
            );

            startActivity(intent);

        } catch (ActivityNotFoundException e) {

            openExternal(webUrl);
        }
    }

    // ================= EXTERNAL LINKS =================

    private void openExternal(String url) {

        if (url == null) return;

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

    // ================= ANDROID BRIDGE =================

    private class AndroidBridge {

        @JavascriptInterface
        public void openSocial(String name) {

            runOnUiThread(
                    () -> MainActivity.this.openSocial(name)
            );
        }

        @JavascriptInterface
        public void openExternal(String url) {

            runOnUiThread(
                    () -> MainActivity.this.openExternal(url)
            );
        }
    }

    private void showMessage(String message) {

        Toast.makeText(
                this,
                message,
                Toast.LENGTH_SHORT
        ).show();
    }

    // ================= BACK BUTTON =================

    @Override
    public void onBackPressed() {

        if (webView != null && webView.canGoBack()) {

            webView.goBack();

        } else {

            super.onBackPressed();
        }
    }

    // ================= CLEANUP =================

    @Override
    protected void onDestroy() {

        handler.removeCallbacksAndMessages(null);

        if (webView != null) {

            webView.removeJavascriptInterface("Android");
            webView.destroy();
            webView = null;
        }

        super.onDestroy();
    }
}
