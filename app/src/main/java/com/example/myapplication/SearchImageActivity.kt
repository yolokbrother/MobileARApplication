package com.example.myapplication

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.get
import com.example.myapplication.databinding.ActivitySearchImageBinding
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class SearchImageActivity : AppCompatActivity() {

    lateinit var binding: ActivitySearchImageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //bottom navigation
        binding = ActivitySearchImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bottomNavigationView.background = null
        bottomNavigationView.menu[2].isEnabled = false
        //bottom navigation//selected
        bottomNavigationView.selectedItemId = R.id.miHome
        bottomNavigationView.setOnItemSelectedListener  {
            when(it.itemId){
                R.id.miHome -> startActivity( Intent(this, MainActivity::class.java))
                R.id.miProfile -> startActivity( Intent(this, ViewPersonalActivity::class.java))
                R.id.miSearch -> startActivity( Intent(this, SearchImageActivity::class.java))
                R.id.miSettings -> startActivity( Intent(this, MainActivity::class.java))
            }
            true
        }

        fab.setOnClickListener{
            startActivity( Intent(this, AddPhotoActivity::class.java))
        }

        binding.getImage.setOnClickListener{
            val imageName = binding.etImageId.text.toString()
            val storageRef = FirebaseStorage.getInstance().reference.child("uploads/$imageName.jpg")

            val localfile = File.createTempFile("tempImage","jpg")
            storageRef.getFile(localfile).addOnSuccessListener {

                val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
                binding.imageview.setImageBitmap(bitmap)

            }.addOnFailureListener{

                Toast.makeText(this,"Failed to retrieve the image", Toast.LENGTH_SHORT).show()

            }
        }
    }
}