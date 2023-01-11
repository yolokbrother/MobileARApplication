package com.example.myapplication

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.Toast
import androidx.core.view.get
import com.example.myapplication.databinding.ActivityViewPersonalBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class ViewPersonalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityViewPersonalBinding
    private lateinit var firebaseAuth : FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private lateinit var user: PersonalData
    private lateinit var dialog : Dialog
    private lateinit var uid : String

    override fun onCreate(savedInstanceState: Bundle?) {
        //bottom navigation
        super.onCreate(savedInstanceState)
        binding = ActivityViewPersonalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bottomNavigationView.background = null
        bottomNavigationView.menu[2].isEnabled = false
        bottomNavigationView.selectedItemId = R.id.miProfile
        bottomNavigationView.setOnItemSelectedListener  {
            when(it.itemId){
                R.id.miHome -> startActivity( Intent(this, MainActivity::class.java))
                R.id.miProfile -> startActivity( Intent(this, ViewPersonalActivity::class.java))
                R.id.miSearch -> startActivity( Intent(this, SearchImageActivity::class.java))
                R.id.miSettings -> startActivity( Intent(this, SettingActivity::class.java))
            }
            true
        }
        fab.setOnClickListener{
            startActivity( Intent(this, AddPhotoActivity::class.java))
        }


        firebaseAuth = FirebaseAuth.getInstance()
        uid = firebaseAuth.currentUser?.uid.toString()

        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        if(uid.isNotEmpty()){

            getUserData()

        }
        binding.updateBtn.setOnClickListener{
            startActivity( Intent(this, AddPersonalActivity::class.java))
        }
    }

    private fun getUserData() {
        showProgressBar()
        databaseReference.child(uid).addValueEventListener(object :ValueEventListener{

            @SuppressLint("SetTextI18n")
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    user = snapshot.getValue(PersonalData::class.java)!!
                    binding.tvFullName.text = user.firstName + " " + user.lastName
                    binding.tvBio.text = user.bio
                    getUserProfile()
                }else{
                    hideProgressBar()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                hideProgressBar()
                Toast.makeText(this@ViewPersonalActivity,"Failed to get User Profile data",Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun getUserProfile() {

        storageReference = FirebaseStorage.getInstance().reference.child("Users/$uid")
        val localFile = File.createTempFile("tempImage","jpg")
        storageReference.getFile(localFile).addOnSuccessListener {

            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            binding.profileImage.setImageBitmap(bitmap)
            hideProgressBar()

        }.addOnFailureListener{
            hideProgressBar()
            Toast.makeText(this@ViewPersonalActivity,"Failed to retrieve image",Toast.LENGTH_SHORT).show()
        }
    }

    private fun showProgressBar(){
        dialog = Dialog(this@ViewPersonalActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_wait)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }

    private fun hideProgressBar(){

        dialog.dismiss()

    }

}