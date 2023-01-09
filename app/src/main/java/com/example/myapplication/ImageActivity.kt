package com.example.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_image.*


class ImageActivity : AppCompatActivity(), ImageAdapter.OnItemClickListener {
    private var mAdapter: ImageAdapter? = null
    private var mDatabaseRef: DatabaseReference? = null
    private var mUploads: MutableList<Upload>? = null
    private var mStorage: FirebaseStorage? = null
    private val mDBListener: ValueEventListener? = null

    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)
        //performance
        recycler_view.setHasFixedSize(true)
        recycler_view.layoutManager = LinearLayoutManager(this)
        mUploads = ArrayList<Upload>()
        mAdapter = ImageAdapter(this@ImageActivity, mUploads as ArrayList<Upload>)

        recycler_view.adapter = mAdapter

        mAdapter!!.setOnItemClickListener(this@ImageActivity)

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

                progress_circle.visibility = View.INVISIBLE
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@ImageActivity, databaseError.message, Toast.LENGTH_SHORT)
                    .show()
                progress_circle.visibility = View.INVISIBLE
            }
        })
    }
    override fun onItemClick(position: Int) {
        Toast.makeText(this, "Normal click at position: $position", Toast.LENGTH_SHORT).show()
    }

    override fun onWhatEverClick(position: Int) {
        Toast.makeText(this, "Whatever click at position: $position", Toast.LENGTH_SHORT).show()
    }

    override fun onDeleteClick(position: Int) {
        val selectedItem = mUploads!![position]
        val selectedKey: String? = selectedItem.mKey

        val imageRef = mStorage!!.getReferenceFromUrl(selectedItem.imageUrl!!)
        imageRef.delete().addOnSuccessListener {
            if (selectedKey != null) {
                mDatabaseRef!!.child(selectedKey).removeValue()
            }
            Toast.makeText(this@ImageActivity, "Item deleted", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mDBListener != null) {
            mDatabaseRef?.removeEventListener(mDBListener)
        }
    }
}