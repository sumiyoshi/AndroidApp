package com.example.sumi.androidapp.interfaceadapter.presenter

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.sumi.androidapp.R


class DetailPresenter : AppCompatActivity() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        val webView = findViewById<WebView>(R.id.web_view)
        val url = intent.getStringExtra("url")

        // リンクをタップしたときに標準ブラウザを起動させない
        webView .webViewClient = WebViewClient()

        // 最初に投稿を表示
        webView .loadUrl(url)

        // jacascriptを許可する
        webView .settings.javaScriptEnabled = true
    }
}
