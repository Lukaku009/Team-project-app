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
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {

    private lateinit var emailText: EditText
    private lateinit var passwordText: EditText
    private lateinit var loginBtn: Button
    private lateinit var mregbtn: Button
    private lateinit var fireAuth: FirebaseAuth
    private lateinit var loginProgress: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        fireAuth = FirebaseAuth.getInstance()
        emailText = findViewById(R.id.loginemail)
        passwordText = findViewById(R.id.loginpassword)
        loginBtn = findViewById(R.id.loginbtn)
        mregbtn = findViewById(R.id.loginregbtn)
        loginProgress = findViewById(R.id.loginprogress)

        loginBtn.setOnClickListener {
            val emailLogin = emailText.text.toString()
            val passLogin = passwordText.text.toString()
            if (!TextUtils.isEmpty(emailLogin) && !TextUtils.isEmpty(passLogin)) {
                loginProgress.visibility = View.VISIBLE

                fireAuth.signInWithEmailAndPassword(emailLogin, passLogin).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        sendToUI()
                    } else {
                        toastMessage("Can't Login. TRY AGAIN WITH CORRECT INFORMATION")
                    }
                    loginProgress.visibility = View.INVISIBLE
                }
            } else {
                toastMessage("Please Enter Email and Password")
            }
        }

        mregbtn.setOnClickListener {
            sendToRegister()
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser: FirebaseUser? = fireAuth.currentUser
        if (currentUser != null) {
            sendToUI()
        }
    }

    private fun sendToUI() {
        val mainIntent = Intent(this@LoginActivity, UIActivity::class.java)
        startActivity(mainIntent)
        finish()
    }

    private fun sendToRegister() {
        val regIntent = Intent(this@LoginActivity, RegisterActivity::class.java)
        startActivity(regIntent)
        finish()
    }

    private fun toastMessage(message: String) {
        Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show()
    }
}
