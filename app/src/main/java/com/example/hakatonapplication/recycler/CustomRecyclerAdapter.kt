package com.example.hakatonapplication.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.hakatonapplication.R

class CustomRecyclerAdapter(
    val imagesArray: ArrayList<ImagesWithDefection>,
    var onItemClick: ((ImagesWithDefection) -> Unit)? = null
) : RecyclerView.Adapter<CustomRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(imagesArray[position], onItemClick)
    }

    override fun getItemCount(): Int {
        return imagesArray.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(image: ImagesWithDefection, onItemClick: ((ImagesWithDefection) -> Unit)?) {
            val imageDefection = itemView.findViewById(R.id.icon) as ImageView
            imageDefection.setImageURI(image.uri)
            itemView.setOnClickListener {
                onItemClick?.invoke(image)
            }
        }
    }
}