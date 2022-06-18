package com.vde.mhvezdekod22re.ui.activity

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.vde.mhvezdekod22re.R
import com.vde.mhvezdekod22re.utils.L

class SplashActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val lottieView = findViewById<LottieAnimationView>(R.id.splashScreenlottieView)
        lottieView.addAnimatorListener(object : Animator.AnimatorListener{
            override fun onAnimationStart(animation: Animator?) {
                L.d("onAnimationStart")
            }

            override fun onAnimationEnd(animation: Animator?) {
                L.d("onAnimationEnd")
                startMainActivity()
            }

            override fun onAnimationCancel(animation: Animator?) {
                L.d("onAnimationCancel")
            }

            override fun onAnimationRepeat(animation: Animator?) {
                L.d("onAnimationRepeat")
            }

        })
    }


    private fun startMainActivity() {
        val mainIntent = Intent(this, MainActivity::class.java)
        mainIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(mainIntent)
        finish()

    }


}