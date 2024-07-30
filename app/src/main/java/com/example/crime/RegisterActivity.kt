package com.example.crime


import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var emailText: EditText
    private lateinit var passwordText: EditText
    private lateinit var registerBtn: Button
    private lateinit var loginBtn: Button
    private lateinit var fireAuth: FirebaseAuth
    private lateinit var registerProgress: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        fireAuth = FirebaseAuth.getInstance()
        emailText = findViewById(R.id.registerEmail)
        passwordText = findViewById(R.id.registerPassword)
        registerBtn = findViewById(R.id.registerBtn)
        loginBtn = findViewById(R.id.registerLoginBtn)
        registerProgress = findViewById(R.id.registerProgress)

        registerBtn.setOnClickListener {
            val email = emailText.text.toString().trim()
            val password = passwordText.text.toString().trim()

            if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                registerProgress.visibility = View.VISIBLE

                fireAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            sendToMain()
                        } else {
                            showToast("Registration Failed. Try Again.")
                        }
                        registerProgress.visibility = View.INVISIBLE
                    }
            } else {
                showToast("Please enter Email and Password")
            }
        }

        loginBtn.setOnClickListener {
            sendToLogin()
        }
    }

    private fun sendToMain() {
        val mainIntent = Intent(this, MainActivity::class.java)
        startActivity(mainIntent)
        finish()
    }

    private fun sendToLogin() {
        val loginIntent = Intent(this, LoginActivity::class.java)
        startActivity(loginIntent)
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
