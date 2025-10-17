package com.example.ms1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

/**
 * This activity provides a user interface for signing in.
 * It includes fields for email and password, a login button, and a button to navigate to the sign-up page.
 */
class LoginActivity : AppCompatActivity() {

    // Firebase Authentication instance to handle user sign-in.
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set the content view to the login activity layout.
        setContentView(R.layout.activity_login)

        // Initialize the Firebase Authentication instance.
        auth = FirebaseAuth.getInstance()

        // Get references to the UI elements from the layout.
        val etEmail = findViewById<EditText>(R.id.et_email)
        val etPassword = findViewById<EditText>(R.id.et_password)
        val btnLogin = findViewById<Button>(R.id.btn_login)
        val btnSignUp = findViewById<Button>(R.id.btn_signup)

        // Set a click listener on the login button.
        btnLogin.setOnClickListener {
            // Get the email and password from the EditText fields.
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            // Validate that the email and password fields are not empty.
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, getString(R.string.please_fill_all_fields), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Attempt to sign in the user with the provided email and password.
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // If sign-in is successful, navigate to the LandingActivity.
                        val intent = Intent(this, LandingActivity::class.java)
                        startActivity(intent)
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                        finish()
                    } else {
                        // If sign-in fails, log the error and display a message to the user.
                        Log.w("LoginActivity", "signInWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, getString(R.string.authentication_failed, task.exception?.message), Toast.LENGTH_LONG).show()
                    }
                }
        }

        // Set a click listener on the sign-up button.
        btnSignUp.setOnClickListener {
            // Navigate to the SignUpActivity.
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }
    }

    /**
     * Overrides the default finish method to add a custom transition animation.
     */
    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }
}