package com.example.crime


import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot

class ViewReportsActivity : AppCompatActivity() {

    private lateinit var reportsRecyclerView: RecyclerView
    private lateinit var reportsProgress: ProgressBar
    private lateinit var firestore: FirebaseFirestore
    private lateinit var reportsAdapter: ReportsAdapter
    private val reportsList = mutableListOf<Report>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_reports)

        firestore = FirebaseFirestore.getInstance()
        reportsRecyclerView = findViewById(R.id.reportsRecyclerView)
        reportsProgress = findViewById(R.id.reportsProgress)

        reportsRecyclerView.layoutManager = LinearLayoutManager(this)
        reportsAdapter = ReportsAdapter(reportsList)
        reportsRecyclerView.adapter = reportsAdapter

        fetchReports()
    }

    private fun fetchReports() {
        reportsProgress.visibility = View.VISIBLE
        firestore.collection("crime_reports").get()
            .addOnCompleteListener { task ->
                reportsProgress.visibility = View.INVISIBLE
                if (task.isSuccessful) {
                    for (document: QueryDocumentSnapshot in task.result) {
                        val report = document.toObject(Report::class.java)
                        reportsList.add(report)
                    }
                    reportsAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(this, "Error Fetching Reports: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
