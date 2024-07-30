package com.example.crime

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val nameTextView: TextView = itemView.findViewById(R.id.textName)
    private val ageTextView: TextView = itemView.findViewById(R.id.textAge)
    private val dressColorTextView: TextView = itemView.findViewById(R.id.textDressColor)
    private val cityTextView: TextView = itemView.findViewById(R.id.textCity)
    private val genderTextView: TextView = itemView.findViewById(R.id.textGender)
    private val descriptionTextView: TextView = itemView.findViewById(R.id.textDescription)
    private val addressTextView: TextView = itemView.findViewById(R.id.textAddress)
    private val imageView: ImageView = itemView.findViewById(R.id.imageView)

    fun setDetails(
        context: Context,
        name: String?,
        age: String?,
        dressColor: String?,
        image: String?,
        city: String?,
        gender: String?,
        description: String?,
        address: String?
    ) {
        nameTextView.text = name
        ageTextView.text = age
        dressColorTextView.text = dressColor
        cityTextView.text = city
        genderTextView.text = gender
        descriptionTextView.text = description
        addressTextView.text = address

        // Load image using Glide
        if (!image.isNullOrEmpty()) {
        } else {
            imageView.setImageResource(R.drawable.placeholder_image) // Set a placeholder image if needed
        }
    }

    interface ClickListener {
        fun onItemClick(view: View, position: Int)
        fun onItemLongClick(view: View, position: Int)
    }

    fun setOnClickListener(clickListener: ClickListener) {
        itemView.setOnClickListener { v -> clickListener.onItemClick(v, adapterPosition) }
        itemView.setOnLongClickListener { v ->
            clickListener.onItemLongClick(v, adapterPosition)
            true
        }
    }
}
