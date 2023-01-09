package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import com.example.myapplication.databinding.ActivityMainBinding
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_main.*
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_image.*


class MainActivity : AppCompatActivity(), ImageAdapter.OnItemClickListener {
    private lateinit var binding: ActivityMainBinding
    private var mAdapter: ImageAdapter? = null
    private var mDatabaseRef: DatabaseReference? = null
    private var mUploads: MutableList<Upload>? = null
    private var mStorage: FirebaseStorage? = null
    private val mDBListener: ValueEventListener? = null
    private lateinit var firebaseAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //bottom navigation
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bottomNavigationView.background = null
        bottomNavigationView.menu[2].isEnabled = false
        //bottom navigation//selected
        bottomNavigationView.selectedItemId = R.id.miHome
        bottomNavigationView.setOnItemSelectedListener  {
                when(it.itemId){
                    R.id.miHome -> startActivity( Intent(this, MainActivity::class.java))
                    R.id.miProfile -> startActivity( Intent(this, ViewPersonalActivity::class.java))
                    R.id.miSearch -> startActivity( Intent(this, MainActivity::class.java))
                    R.id.miSettings -> startActivity( Intent(this, MainActivity::class.java))
                }
            true
        }

        fab.setOnClickListener{
            startActivity( Intent(this, AddPhotoActivity::class.java))
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
        val selectedItem = mUploads!![position]
        val selectedName: String? = selectedItem.name
        Toast.makeText(this, "Photo selected $selectedName", Toast.LENGTH_SHORT).show()
    }

    override fun onDeleteClick(position: Int) {
        TODO()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mDBListener != null) {
            mDatabaseRef?.removeEventListener(mDBListener)
        }
    }

}