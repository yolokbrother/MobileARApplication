package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.get
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.databinding.ActivitySettingBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.activity_setting.bottomNavigationView
import kotlinx.android.synthetic.main.activity_setting.fab

class SettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingBinding

    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        //bottom navigation
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bottomNavigationView.background = null
        bottomNavigationView.menu[2].isEnabled = false
        //bottom navigation//selected
        bottomNavigationView.selectedItemId = R.id.miHome
        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.miHome -> startActivity(Intent(this, MainActivity::class.java))
                R.id.miProfile -> startActivity(Intent(this, ViewPersonalActivity::class.java))
                R.id.miSearch -> startActivity(Intent(this, SearchImageActivity::class.java))
                R.id.miSettings -> startActivity(Intent(this, SettingActivity::class.java))
            }
            true
        }

        fab.setOnClickListener {
            startActivity(Intent(this, AddPhotoActivity::class.java))
        }

        var nightMODE = false
        var sharedPreferences: SharedPreferences
        var editor: SharedPreferences.Editor

        //shared preferences to save mode when exit app
        sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE)
        nightMODE = sharedPreferences.getBoolean("night",false)
        if(nightMODE){
            switcher.isChecked
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        //switcher activity
        switcher.setOnClickListener{
            if(nightMODE){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                editor = sharedPreferences.edit()
                editor.putBoolean("night",false)
            }else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                editor = sharedPreferences.edit()
                editor.putBoolean("night",true)
            }
            editor.apply()
        }
    }
}