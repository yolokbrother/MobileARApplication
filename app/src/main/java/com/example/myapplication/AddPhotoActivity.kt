package com.example.myapplication

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.get
import com.example.myapplication.databinding.ActivityAddPersonalBinding
import com.example.myapplication.databinding.ActivityAddPhotoBinding
import kotlinx.android.synthetic.main.activity_add_photo.*
import kotlinx.android.synthetic.main.activity_add_photo.view.fab
import kotlinx.android.synthetic.main.activity_main.bottomNavigationView
import kotlinx.android.synthetic.main.activity_main.fab

class AddPhotoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddPhotoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        //bottom navigation
        super.onCreate(savedInstanceState)
        binding = ActivityAddPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bottomNavigationView.background = null
        bottomNavigationView.menu[2].isEnabled = false
        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.miHome -> startActivity(Intent(this, MainActivity::class.java))
                R.id.miProfile -> startActivity(Intent(this, ViewPersonalActivity::class.java))
                R.id.miSearch -> startActivity(Intent(this, SearchImageActivity::class.java))
                R.id.miSettings -> startActivity(Intent(this, MainActivity::class.java))
            }
            true
        }
        fab.setOnClickListener {
            startActivity(Intent(this, AddPhotoActivity::class.java))
        }

        btnOpenCamera.setOnClickListener {
            startActivity(Intent(this, CameraActivity::class.java))

        }

        btnUploadPhoto.setOnClickListener{
            startActivity(Intent(this, UploadPhotoActivity::class.java))

        }
    }
}