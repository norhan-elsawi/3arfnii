package com.ibtikar.a3arfnii.applicationActivities.submitDetails.complaintDetails.mvp.adapter

import android.content.Context
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.ibtikar.a3arfnii.R
import com.squareup.picasso.Picasso
import java.io.File


class ImageAdapter(private var imgUri: List<File>, private var context: CallBack) : RecyclerView.Adapter<ImageAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var img: ImageView? = null
        var delete: ImageView? = null

        init {
            img = itemView.findViewById(R.id.img)
            delete = itemView.findViewById(R.id.delete)
            delete?.setOnClickListener { _ ->
                context.deleteImage(adapterPosition)
            }

        }
    }

    override fun getItemCount(): Int {
        return imgUri.size
    }

    override fun onBindViewHolder(holder: MyViewHolder?, position: Int) {
        Picasso.with(context as Context).load(imgUri[position]).into(holder?.img)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent?.getContext())
                .inflate(R.layout.image_item, parent, false)

        return MyViewHolder(itemView)
    }


}
