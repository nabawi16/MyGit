package com.nabawi.mygit.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import com.nabawi.mygit.R

class SplashActivity : AppCompatActivity() {

    private val splashTimeOut: Long = 500

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        setContentView(R.layout.activity_splash)

        val splashImage = findViewById<ImageView>(R.id.splash_image)

        val animation = AnimationUtils.loadAnimation(this, R.anim.fade_in)

        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                Handler().postDelayed({
                    val mainIntent = Intent(this@SplashActivity, MainActivity::class.java)
                    startActivity(mainIntent)
                    finish()
                }, splashTimeOut)
            }

            override fun onAnimationRepeat(animation: Animation?) {

            }
        })
        splashImage.startAnimation(animation)
    }
}