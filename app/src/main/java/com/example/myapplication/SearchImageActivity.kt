package com.example.myapplication

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.myapplication.databinding.ActivitySearchImageBinding
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_search_image.*
import java.io.File

class SearchImageActivity : AppCompatActivity() {

    lateinit var binding: ActivitySearchImageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //bottom navigation
        binding = ActivitySearchImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        shareImage.setOnClickListener{
            val myIntent = Intent(Intent.ACTION_SEND)
            myIntent.type = "type/palin"
            val shareBody = "body"
            val shareSub = "subject"
            myIntent.putExtra(Intent.EXTRA_SUBJECT,shareBody)
            myIntent.putExtra(Intent.EXTRA_TEXT,shareSub)
            startActivity(Intent.createChooser(myIntent,"Share your picture"))
        }
    }
}