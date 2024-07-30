package com.example.crime

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class PostActivity : AppCompatActivity() {

    private lateinit var mDatabase: DatabaseReference

    private lateinit var editName: EditText
    private lateinit var editAge: EditText
    private lateinit var editDressColor: EditText
    private lateinit var editImage: EditText
    private lateinit var editCity: EditText
    private lateinit var editGender: EditText
    private lateinit var editDescription: EditText
    private lateinit var editAddress: EditText
    private lateinit var buttonSubmit: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        // Initialize Firebase
        mDatabase = FirebaseDatabase.getInstance().getReference("Admin_Missing")

        // Initialize UI elements
        editName = findViewById(R.id.editName)
        editAge = findViewById(R.id.editAge)
        editDressColor = findViewById(R.id.editDressColor)
        editImage = findViewById(R.id.editImage)
        editCity = findViewById(R.id.editCity)
        editGender = findViewById(R.id.editGender)
        editDescription = findViewById(R.id.editDescription)
        editAddress = findViewById(R.id.editAddress)
        buttonSubmit = findViewById(R.id.buttonSubmit)

        buttonSubmit.setOnClickListener { submitPost() }
    }

    private fun submitPost() {
        // Get input from EditText fields
        val name = editName.text.toString().trim()
        val age = editAge.text.toString().trim()
        val dressColor = editDressColor.text.toString().trim()
        val image = editImage.text.toString().trim()
        val city = editCity.text.toString().trim()
        val gender = editGender.text.toString().trim()
        val description = editDescription.text.toString().trim()
        val address = editAddress.text.toString().trim()

        // Validate input
        if (name.isEmpty() || age.isEmpty() || dressColor.isEmpty() || image.isEmpty() || city.isEmpty() || gender.isEmpty() || description.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Create a new Missing object
        val missingId = mDatabase.push().key
        val missing = Missing(name, age, dressColor, image, city, gender, description, address)

        if (missingId != null) {
            mDatabase.child(missingId).setValue(missing).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Post submitted successfully", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Failed to submit post", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, "Failed to generate post ID", Toast.LENGTH_SHORT).show()
        }
    }
}
