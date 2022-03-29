package com.android.simple.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.android.simple.R
import com.android.simple.view.biometric.BiometricActivity
import com.github.hueyra.webview.WebActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<View>(R.id.btn_action1).setOnClickListener {
            startActivity(Intent(this, AlbumCameraActivity::class.java))
        }
        findViewById<View>(R.id.btn_action2).setOnClickListener {
            startActivity(Intent(this, BiometricActivity::class.java))
        }
        findViewById<View>(R.id.btn_action3).setOnClickListener {
            startActivity(Intent(this, WebActivity::class.java).apply {
                putExtra("url", "https://www.wanandroid.com/")
            })
        }
    }

}