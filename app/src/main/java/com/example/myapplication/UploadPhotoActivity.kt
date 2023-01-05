package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityUploadPhotoBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_upload_photo.*

class UploadPhotoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadPhotoBinding
    private lateinit var firebaseAuth : FirebaseAuth
    private var mImageUri: Uri? = null
    private var mStorageRef: StorageReference? = null
    private var mDatabaseRef: DatabaseReference? = null
    private var mUploadTask: StorageTask<*>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //save image in file (uploads) on storage
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads")
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads")
        //get users
        firebaseAuth = FirebaseAuth.getInstance()


        selectImagebtn!!.setOnClickListener { openFileChooser() }
        uploadimagebtn!!.setOnClickListener {
            if (mUploadTask != null && mUploadTask!!.isInProgress) {
            Toast.makeText(this@UploadPhotoActivity, "Upload in progress", Toast.LENGTH_SHORT).show()
        } else {
            uploadFile()
        }}
        btnShowUpload.setOnClickListener{startActivity( Intent(this, ImageActivity::class.java))}
    }

    //return File Extension
    private fun getFileExtension(uri: Uri): String? {
        val cR = contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cR.getType(uri))
    }

    private fun uploadFile() {
        if (mImageUri != null) {
            val fileReference = mStorageRef!!.child(System.currentTimeMillis().toString() + "." + getFileExtension(mImageUri!!)
            )
            mUploadTask = fileReference.putFile(mImageUri!!)
                .addOnSuccessListener { taskSnapshot ->
                    Handler(Looper.getMainLooper()).postDelayed({ progress_bar!!.progress = 0 }, 500)
                    Toast.makeText(this@UploadPhotoActivity, "Upload successful", Toast.LENGTH_LONG).show()

//                    val upload = Upload(
//                        edit_text_file_name!!.text.toString().trim { it <= ' ' },
//                        taskSnapshot.metadata?.reference?.downloadUrl.toString(),
//                        firebaseAuth.currentUser?.uid.toString()
//                    )
//                    val uploadId = mDatabaseRef!!.push().key
//                    mDatabaseRef!!.child((uploadId)!!).setValue(upload)
                    //uri not found fix with com.examples....
                    val urlTask = taskSnapshot.storage.downloadUrl
                    while (!urlTask.isSuccessful);
                    val downloadUrl = urlTask.result
                    Log.i("CheckingUri",downloadUrl.toString())
                    val upload = Upload(
                        edit_text_file_name.text.toString().trim(),
                        downloadUrl.toString(),
                        firebaseAuth.currentUser?.uid.toString()
                    )
                    val uploadId = mDatabaseRef!!.push().key
                    mDatabaseRef!!.child(uploadId!!).setValue(upload)
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this@UploadPhotoActivity, e.message, Toast.LENGTH_SHORT).show()
                }
                .addOnProgressListener { taskSnapshot ->
                    val progress =
                        (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount)
                    progress_bar!!.progress = progress.toInt()
                }
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show()
        }
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