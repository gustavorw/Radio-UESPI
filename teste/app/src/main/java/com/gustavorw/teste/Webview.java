package com.gustavorw.teste;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class Webview extends AppCompatActivity {
    private WebView newsWeb;
    private ProgressBar progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        newsWeb = findViewById(R.id.newsWebView);

        progress = findViewById(R.id.progress_web);
        progress.setMax(100);
        Intent intent = getIntent();
        newsWeb.getSettings().setJavaScriptEnabled(true);
        String url = intent.getStringExtra("link");
        Log.v("link",url);
        // eliminar a verificação de ssl
        newsWeb.setWebViewClient(new WebViewClient(){
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
        });

        newsWeb.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progress.setProgress(newProgress);
                if(newProgress == 100){
                    progress.setVisibility(View.GONE);
                }
            }

        });
        newsWeb.loadUrl(url);
    }
}
