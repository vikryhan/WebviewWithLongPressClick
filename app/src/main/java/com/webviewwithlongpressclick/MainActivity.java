package com.webviewwithlongpressclick;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.View;
import android.webkit.URLUtil;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    WebView webView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webView = (WebView) findViewById(R.id.webView);

        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.loadUrl("https://www.materinesia.com/");

        registerForContextMenu(webView);
    }
    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        super.onCreateContextMenu(contextMenu, view, contextMenuInfo);
        final WebView.HitTestResult hitTestResult = webView.getHitTestResult();

        if (hitTestResult.getType() == WebView.HitTestResult.IMAGE_TYPE ||
                hitTestResult.getType() == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {

            contextMenu.setHeaderTitle("Download Gambar");
            contextMenu.setHeaderIcon(R.drawable.ic_download);
            contextMenu.add(0, 1, 0, "Download").setOnMenuItemClickListener(menuItem -> {
                String DownloadGambar = hitTestResult.getExtra();
                if (URLUtil.isValidUrl(DownloadGambar)) {
                    DownloadManager.Request mRequest = new DownloadManager.Request(Uri.parse(DownloadGambar));
                    mRequest.allowScanningByMediaScanner();
                    mRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    DownloadManager mDownloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        Objects.requireNonNull(mDownloadManager).enqueue(mRequest);
                    }
                    Toast.makeText(MainActivity.this, "Gambar sukses di unduh", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, "Gambar gagal di unduh", Toast.LENGTH_LONG).show();
                }
                return false;
            });
        }
    }
}