package com.example.myapplication

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Window
import android.widget.Toast
import androidx.core.view.get
import com.example.myapplication.databinding.ActivityAddPersonalBinding
import com.example.myapplication.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_main.*

class AddPersonalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddPersonalBinding
    private lateinit var firebaseAuth : FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private lateinit var imageUri: Uri
    private lateinit var dialog : Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //bottom navigation
        binding = ActivityAddPersonalBinding.inflate(layoutInflater)
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
        //ActivityAddPersonal
        firebaseAuth = FirebaseAuth.getInstance()
        val uid = firebaseAuth.currentUser?.uid
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        binding.saveBtn.setOnClickListener {

            showProgressBar()
            val firstName = binding.etFirstName.text.toString()
            val lastName = binding.etLastName.text.toString()
            val bio = binding.etBio.text.toString()

            val user = PersonalData(firstName,lastName,bio)
            if(uid != null){
                databaseReference.child(uid).setValue(user).addOnCompleteListener {
                    if(it.isSuccessful){

                        uploadProfilePic()

                    }else{

                        hideProgressBar()
                        Toast.makeText(this@AddPersonalActivity,"Failed to update profile", Toast.LENGTH_SHORT).show()

                    }
                }
            }
        }
    }

    private fun  uploadProfilePic(){
        imageUri = Uri.parse("android.resource://$packageName/${R.drawable.ic_person}")
        storageReference = FirebaseStorage.getInstance().getReference("Users/"+ firebaseAuth.currentUser?.uid)
        storageReference.putFile(imageUri).addOnSuccessListener{

            hideProgressBar()
            Toast.makeText(this@AddPersonalActivity,"Profile successfully updated", Toast.LENGTH_SHORT).show()

        }.addOnFailureListener{

            hideProgressBar()
            Toast.makeText(this@AddPersonalActivity,"Failed to upload the image", Toast.LENGTH_SHORT).show()

        }
    }

    private fun showProgressBar(){
        dialog = Dialog(this@AddPersonalActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_wait)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }

    private fun hideProgressBar(){

        dialog.dismiss()

    }
}

