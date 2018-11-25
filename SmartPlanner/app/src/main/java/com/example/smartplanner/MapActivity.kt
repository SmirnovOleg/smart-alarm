package com.example.smartplanner

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.*
import android.webkit.WebSettings
import android.webkit.WebView
import kotlinx.android.synthetic.main.activity_map.*

import java.io.IOException
import java.io.InputStream

class MapActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_map)
        super.onCreate(savedInstanceState)

        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true

        try {
            val stream = assets.open("index.html")
            val buffer = ByteArray(stream.available())
            stream.read(buffer)
            stream.close()

            val htmlText = String(buffer)
            webView.loadDataWithBaseURL(
                "http://ru.yandex.api.yandexmapswebviewexample.ymapapp",
                htmlText,
                "text/html",
                "UTF-8",
                null
            )
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }
}
