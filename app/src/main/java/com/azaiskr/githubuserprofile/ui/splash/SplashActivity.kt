package com.azaiskr.githubuserprofile.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.azaiskr.githubuserprofile.databinding.ActivitySplashBinding
import com.azaiskr.githubuserprofile.domain.SettingPreferences
import com.azaiskr.githubuserprofile.domain.dataStore
import com.azaiskr.githubuserprofile.helper.VmFactoryPref
import com.azaiskr.githubuserprofile.ui.main.MainActivity
import com.azaiskr.githubuserprofile.ui.main.UserListViewModel

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val preferences = SettingPreferences.getInstance(application.dataStore)
        val userListViewModel =
            ViewModelProvider(this, VmFactoryPref(preferences))[UserListViewModel::class.java]
        userListViewModel.getMode().observe(this) {
            if (it) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        val delay = 2000L
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, delay)
    }
}