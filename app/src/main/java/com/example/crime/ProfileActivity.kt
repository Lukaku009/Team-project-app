package com.example.crime

import android.content.Context
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileActivity : AppCompatActivity() {

    private lateinit var nameTextView: TextView
    private lateinit var ageTextView: TextView
    private lateinit var dressColorTextView: TextView
    private lateinit var cityTextView: TextView
    private lateinit var genderTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var addressTextView: TextView
    private lateinit var imageView: ImageView

    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        nameTextView = findViewById(R.id.textName)
        ageTextView = findViewById(R.id.textAge)
        dressColorTextView = findViewById(R.id.textDressColor)
        cityTextView = findViewById(R.id.textCity)
        genderTextView = findViewById(R.id.textGender)
        descriptionTextView = findViewById(R.id.textDescription)
        addressTextView = findViewById(R.id.textAddress)
        imageView = findViewById(R.id.imageView)

        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        loadUserProfile()
    }

    private fun loadUserProfile() {
        val user = auth.currentUser
        user?.let {
            val userId = user.uid
            val docRef = firestore.collection("users").document(userId)
            docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        nameTextView.text = document.getString("name")
                        ageTextView.text = document.getString("age")
                        dressColorTextView.text = document.getString("dressColor")
                        cityTextView.text = document.getString("city")
                        genderTextView.text = document.getString("gender")
                        descriptionTextView.text = document.getString("description")
                        addressTextView.text = document.getString("address")
                        val imageUrl = document.getString("imageUrl")
                        if (!imageUrl.isNullOrEmpty()) {
                        } else {
                            imageView.setImageResource(R.drawable.placeholder_image) // Set a placeholder image if needed
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle the error
                }
        }
    }
}
