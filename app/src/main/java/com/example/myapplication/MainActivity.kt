package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.MenuItem.OnMenuItemClickListener
import androidx.core.view.get
import kotlinx.android.synthetic.main.activity_main.*
import com.example.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //bottom navigation
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bottomNavigationView.background = null
        bottomNavigationView.menu[2].isEnabled = false
        bottomNavigationView.setOnItemSelectedListener  {
                when(it.itemId){
                    R.id.miHome -> startActivity( Intent(this, MainActivity::class.java))
                    R.id.miProfile -> startActivity( Intent(this, AddPersonalActivity::class.java))
                    R.id.miSearch -> startActivity( Intent(this, MainActivity::class.java))
                    R.id.miSettings -> startActivity( Intent(this, MainActivity::class.java))
                }
            true
        }
    }

}