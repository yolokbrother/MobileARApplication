package com.example.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class ImageAdapter(private val mContext: Context, uploads: List<Upload>) :
    RecyclerView.Adapter<ImageAdapter.ImageViewHolder?>() {
    private val mUploads: List<Upload>

    init {
        mUploads = uploads
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val v: View = LayoutInflater.from(mContext).inflate(R.layout.image_item, parent, false)
        return ImageViewHolder(v)
    }

    //put data into recycle cards
    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val uploadCurrent: Upload = mUploads[position]
        holder.textViewName.text = uploadCurrent.name
        Picasso.with(mContext)
            .load(uploadCurrent.imageUrl)
            .placeholder(R.mipmap.ic_launcher)
            .fit()
            .centerCrop()
            .into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return mUploads.size
    }

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textViewName: TextView
        var imageView: ImageView

        init {
            textViewName = itemView.findViewById<TextView>(R.id.text_view_name)
            imageView = itemView.findViewById(R.id.image_view_upload)
        }
    }


}