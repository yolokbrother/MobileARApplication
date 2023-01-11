package com.example.myapplication

import android.content.Context
import android.view.*
import android.view.ContextMenu.ContextMenuInfo
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso


class ImageAdapter(private val mContext: Context, uploads: List<Upload>) :
    RecyclerView.Adapter<ImageAdapter.ImageViewHolder?>() {
    private val mUploads: List<Upload>
    private lateinit var firebaseAuth : FirebaseAuth
    private var mListener: OnItemClickListener? = null

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
                .load(uploadCurrent.imageUrl.toString())
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(holder.imageView)

    }

    override fun getItemCount(): Int {
        return mUploads.size
    }

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener, View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        var textViewName: TextView
        var imageView: ImageView

        init {
            textViewName = itemView.findViewById<TextView>(R.id.text_view_name)
            imageView = itemView.findViewById(R.id.image_view_upload)
            itemView.setOnClickListener(this)
            itemView.setOnCreateContextMenuListener(this)
        }

        override fun onClick(v: View) {
            if (mListener != null) {
                val position: Int = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    mListener!!.onItemClick(position)
                }
            }
        }

        override fun onCreateContextMenu(menu: ContextMenu, v: View?, menuInfo: ContextMenuInfo?) {
            menu.setHeaderTitle("Select Action")
            val delete = menu.add(Menu.NONE, 1, 1, "Delete")
            delete.setOnMenuItemClickListener(this)
        }

        override fun onMenuItemClick(item: MenuItem): Boolean {
            if (mListener != null) {
                val position: Int = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    when (item.itemId) {
                        1 -> {
                            mListener!!.onDeleteClick(position)
                            return true
                        }
                    }
                }
            }
            return false
        }
    }

        interface OnItemClickListener {
            fun onItemClick(position: Int)
            fun onDeleteClick(position: Int)
        }


        fun setOnItemClickListener(listener: ImageActivity) {
            mListener = listener
        }

        fun setOnItemClickListener(listener: MainActivity) {
            mListener = listener
        }


}