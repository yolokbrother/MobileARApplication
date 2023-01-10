package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.snapshots
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_image.*
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), ImageAdapter.OnItemClickListener {
    private lateinit var binding: ActivityMainBinding
    private var mAdapter: ImageAdapter? = null
    private var mDatabaseRef: DatabaseReference? = null
    private var mUploads: MutableList<Upload>? = null
    private var mStorage: FirebaseStorage? = null
    private val mDBListener: ValueEventListener? = null
    private lateinit var storageReference: StorageReference
    private lateinit var uid : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //bottom navigation
        binding = ActivityMainBinding.inflate(layoutInflater)
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

        //grid layout
        //performance
        recycler_view_Main.setHasFixedSize(true)
        recycler_view_Main.layoutManager = LinearLayoutManager(this)
        mUploads = ArrayList<Upload>()
        mAdapter = ImageAdapter(this@MainActivity, mUploads as ArrayList<Upload>)

        recycler_view_Main.adapter = mAdapter

        mAdapter!!.setOnItemClickListener(this@MainActivity)

        mStorage = FirebaseStorage.getInstance()
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads")

        mDatabaseRef!!.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                //get image information from firebase
                (mUploads as ArrayList<Upload>).clear();

                for (postSnapshot in dataSnapshot.children) {
                    val upload: Upload? = postSnapshot.getValue(Upload::class.java)
                    upload?.setKey(postSnapshot.key)

                    if (upload != null) {
                        mUploads!!.add(upload)
                    }
                }

                mAdapter!!.notifyDataSetChanged()

                progress_circle_Main.visibility = View.INVISIBLE
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@MainActivity, databaseError.message, Toast.LENGTH_SHORT)
                    .show()
                progress_circle_Main.visibility = View.INVISIBLE
            }
        })
    }
    override fun onItemClick(position: Int) {
        val artistId = mUploads?.get(position)?.uid
        mDatabaseRef = FirebaseDatabase.getInstance().reference.child("Users/$artistId")
        mDatabaseRef!!.get().addOnSuccessListener {
            if (it.exists()){
                val firstName = it.child("firstName").value
                val lastName = it.child("lastName").value
                Toast.makeText(this, "Artist Name: $firstName $lastName", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this,"User not exist", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener{
            Toast.makeText(this,"Failed", Toast.LENGTH_SHORT).show()
        }


    }


    override fun onDeleteClick(position: Int) {

    }

    override fun onDestroy() {
        super.onDestroy()
        if (mDBListener != null) {
            mDatabaseRef?.removeEventListener(mDBListener)
        }
    }
    }