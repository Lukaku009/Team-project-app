package com.example.crime


import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class ReportCrimeActivity : AppCompatActivity() {

    private lateinit var crimeDescription: EditText
    private lateinit var reportBtn: Button
    private lateinit var reportProgress: ProgressBar
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_crime)

        firestore = FirebaseFirestore.getInstance()
        crimeDescription = findViewById(R.id.crimeDescription)
        reportBtn = findViewById(R.id.reportBtn)
        reportProgress = findViewById(R.id.reportProgress)

        reportBtn.setOnClickListener {
            val description = crimeDescription.text.toString()
            if (!TextUtils.isEmpty(description)) {
                reportProgress.visibility = View.VISIBLE
                val crimeReport = hashMapOf(
                    "description" to description,
                    "timestamp" to System.currentTimeMillis()
                )
                firestore.collection("crime_reports").add(crimeReport)
                    .addOnCompleteListener { task ->
                        reportProgress.visibility = View.INVISIBLE
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Crime Reported Successfully", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            Toast.makeText(this, "Error Reporting Crime: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Please Enter a Description", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
