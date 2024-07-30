package com.example.crime


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query

class MissingActivity : AppCompatActivity() {

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mFirebaseDatabase: FirebaseDatabase
    private lateinit var mRef: DatabaseReference
    private lateinit var mLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_missing)

        mFirebaseDatabase = FirebaseDatabase.getInstance()
        mRef = mFirebaseDatabase.getReference("Admin_Missing")

        mRecyclerView = findViewById(R.id.recyclerView)
        mRecyclerView.setHasFixedSize(true)
        mLayoutManager = LinearLayoutManager(this)
        mRecyclerView.layoutManager = mLayoutManager

        fetchMissingReports()
    }

    private fun fetchMissingReports() {
        val query: Query = mRef.orderByChild("name")

        val options = FirebaseRecyclerOptions.Builder<Missing>()
            .setQuery(query, Missing::class.java)
            .build()

        val firebaseRecyclerAdapter = object : FirebaseRecyclerAdapter<Missing, ViewHolder>(options) {
            override fun onBindViewHolder(viewHolder: ViewHolder, position: Int, model: Missing) {
                viewHolder.setDetails(
                    applicationContext,
                    model.name,
                    model.age,
                    model.dresscolor,
                    model.image,
                    model.city,
                    model.gender,
                    model.description,
                    model.address
                )
            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
                // Inflate your custom layout for each item in the list
                val view = LayoutInflater.from(parent.context).inflate(R.layout.row2, parent, false)
                return ViewHolder(view)
            }
        }

        mRecyclerView.adapter = firebaseRecyclerAdapter
        firebaseRecyclerAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        (mRecyclerView.adapter as? FirebaseRecyclerAdapter<*, *>?)?.stopListening()
    }
}
