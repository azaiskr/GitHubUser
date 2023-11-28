package com.azaiskr.githubuserprofile.ui.setting

import android.os.Bundle
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.azaiskr.githubuserprofile.R
import com.azaiskr.githubuserprofile.databinding.ActivitySettingBinding
import com.azaiskr.githubuserprofile.domain.SettingPreferences
import com.azaiskr.githubuserprofile.domain.dataStore
import com.azaiskr.githubuserprofile.helper.VmFactoryPref
import com.azaiskr.githubuserprofile.ui.main.UserListViewModel
import com.google.android.material.materialswitch.MaterialSwitch

class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding
    private lateinit var materialSwitch: MaterialSwitch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val section = "Setting"
        supportActionBar?.title = section

        materialSwitch = binding.switchMode
        materialSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                setDarkMode()
            } else {
                setLightMode()
            }
        }

        val viewModel = ViewModelProvider(
            this,
            VmFactoryPref(preferences = SettingPreferences.getInstance(application.dataStore))
        )[UserListViewModel::class.java]
        viewModel.getMode().observe(this) {
            if (it) {
                setDarkMode()
            } else {
                setLightMode()
            }
        }


        materialSwitch.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            viewModel.setMode(isChecked)
        }
    }

    private fun setDarkMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        materialSwitch.isChecked = true
        materialSwitch.setThumbIconResource(R.drawable.dark_mode_fill1)
    }

    private fun setLightMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        materialSwitch.isChecked = false
    }
}