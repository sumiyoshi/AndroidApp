package com.example.sumi.androidapp.interfaceadapter.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.sumi.androidapp.R

class DetailActivity : AppCompatActivity() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        findViewById<WebView>(R.id.web_view).apply {
            // リンクをタップしたときに標準ブラウザを起動させない
            webViewClient = WebViewClient()

            // 最初に投稿を表示
            loadUrl(intent.getStringExtra("url"))

            // javaScriptを許可する
            settings.javaScriptEnabled = true
        }
    }
}
