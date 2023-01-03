package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityUploadPhotoBinding
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_upload_photo.*

class UploadPhotoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadPhotoBinding
    private var mImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        selectImagebtn!!.setOnClickListener { openFileChooser() }
        uploadimagebtn!!.setOnClickListener { }
        btnShowUpload.setOnClickListener(View.OnClickListener { })
    }

    private fun openFileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        resultLauncher.launch(intent)
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val myData: Intent? = result.data
                if (myData != null) {
                    mImageUri = myData.data
                    Picasso.with(this).load(mImageUri).into(firebaseimage)
                }
            }
        }


}