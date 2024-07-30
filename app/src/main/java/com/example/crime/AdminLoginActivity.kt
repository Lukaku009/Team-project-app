package com.example.crime


import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class AdminLoginActivity : AppCompatActivity() {

    private lateinit var emailText: EditText
    private lateinit var passwordText: EditText
    private lateinit var loginBtn: Button
    // private lateinit var mregbtn: Button
    private lateinit var fireAuth: FirebaseAuth
    private lateinit var loginProgress: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_login)

        fireAuth = FirebaseAuth.getInstance()
        emailText = findViewById(R.id.loginemail)
        passwordText = findViewById(R.id.loginpassword)
        loginBtn = findViewById(R.id.loginbtn)
        // mregbtn = findViewById(R.id.loginregbtn)
        loginProgress = findViewById(R.id.loginprogress)

        loginBtn.setOnClickListener {
            val emailLogin = emailText.text.toString()
            val passLogin = passwordText.text.toString()

            if (!TextUtils.isEmpty(emailLogin) && !TextUtils.isEmpty(passLogin)) {
                loginProgress.visibility = View.VISIBLE

                fireAuth.signInWithEmailAndPassword(emailLogin, passLogin).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        sendToAdmin()
                    } else {
                        toastMessage("Can't Login. TRY AGAIN WITH CORRECT INFORMATION")
                    }
                    loginProgress.visibility = View.INVISIBLE
                }
            } else {
                toastMessage("Please Enter Admin Email and Password")
            }
        }
    }

    override fun onStart() {
        super.onStart()
        /*
        val currentUser = fireAuth.currentUser
        if (currentUser != null) {
            sendToMain()
        }
        */
    }

    private fun sendToAdmin() {
        val mainIntent = Intent(this, AdminCrimeActivity::class.java)
        startActivity(mainIntent)
        finish()
    }

    private fun toastMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
