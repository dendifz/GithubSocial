package com.dicoding.githubsocial.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.dicoding.githubsocial.R
import com.dicoding.githubsocial.ui.ViewModelFactory
import com.dicoding.githubsocial.ui.main.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CustomSplashScreen : AppCompatActivity() {

    private lateinit var splashViewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        initVM()
        val intentToDetail = Intent(this@CustomSplashScreen, MainActivity::class.java)
        val handler = Handler(mainLooper)
        lifecycleScope.launch(Dispatchers.Default) {
            handler.postDelayed({
                splashViewModel.getThemeSettings()
                    .observe(this@CustomSplashScreen) { isDarkModeActive ->
                        if (isDarkModeActive) {
                            startActivity(intentToDetail)
                            finish()
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        } else {
                            startActivity(intentToDetail)
                            finish()
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        }
                    }
            }, time.toLong())
        }

    }

    private fun initVM() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        val defineViewModel: SplashViewModel by viewModels { factory }
        splashViewModel = defineViewModel
    }

    companion object {
        private const val time: Int = 5000
    }
}