package com.example.crime


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class UIActivity : AppCompatActivity() {

    private lateinit var logoutBtn: Button
    private lateinit var reportCrimeBtn: Button
    private lateinit var viewReportsBtn: Button
    private lateinit var fireAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ui)

        fireAuth = FirebaseAuth.getInstance()
        logoutBtn = findViewById(R.id.logoutBtn)
        reportCrimeBtn = findViewById(R.id.reportCrimeBtn)
        viewReportsBtn = findViewById(R.id.viewReportsBtn)

        logoutBtn.setOnClickListener {
            fireAuth.signOut()
            sendToLogin()
        }

        reportCrimeBtn.setOnClickListener {
            sendToReportCrime()
        }

        viewReportsBtn.setOnClickListener {
            sendToViewReports()
        }
    }

    private fun sendToLogin() {
        val loginIntent = Intent(this, LoginActivity::class.java)
        startActivity(loginIntent)
        finish()
    }

    private fun sendToReportCrime() {
        val reportCrimeIntent = Intent(this, ReportCrimeActivity::class.java)
        startActivity(reportCrimeIntent)
    }

    private fun sendToViewReports() {
        val viewReportsIntent = Intent(this, ViewReportsActivity::class.java)
        startActivity(viewReportsIntent)
    }
}
