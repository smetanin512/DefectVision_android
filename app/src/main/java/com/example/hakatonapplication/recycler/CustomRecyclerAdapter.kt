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

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(imagesArray[position], onItemClick)
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return imagesArray.size
    }

    //the class is hodling the list view
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