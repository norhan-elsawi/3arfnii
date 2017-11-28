package com.ibtikar.a3arfnii.applicationActivities.splashScreen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import com.ibtikar.a3arfnii.R
import com.ibtikar.a3arfnii.applicationActivities.mainPage.mvp.MainPage


class SplashScreen : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val handler = Handler()
        handler.postDelayed({
            var intent = Intent(this, MainPage::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }
}
