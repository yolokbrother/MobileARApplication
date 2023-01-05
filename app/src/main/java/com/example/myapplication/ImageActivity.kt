package com.example.myapplication

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_image.*
import java.util.ArrayList

class ImageActivity : AppCompatActivity() {
    private var mAdapter: ImageAdapter? = null
    private var mDatabaseRef: DatabaseReference? = null
    private var mUploads: MutableList<Upload>? = null

    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        //performance
        recycler_view.setHasFixedSize(true)
        recycler_view.layoutManager = LinearLayoutManager(this)

        mUploads = ArrayList<Upload>()
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads")
        mDatabaseRef!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {
                    val upload: Upload? = postSnapshot.getValue(Upload::class.java)
                    if (upload != null) {
                        mUploads!!.add(upload)
                    }
                }
                mAdapter = ImageAdapter(this@ImageActivity, mUploads as ArrayList<Upload>)
                recycler_view.adapter = mAdapter
                progress_circle.visibility = View.INVISIBLE
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@ImageActivity, databaseError.message, Toast.LENGTH_SHORT)
                    .show()
                progress_circle.visibility = View.INVISIBLE
            }
        })
    }
}