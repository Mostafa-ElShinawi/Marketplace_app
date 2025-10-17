package com.example.ms1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

/**
 * This activity provides a user interface for signing up.
 * It includes fields for name, address, phone number, email, and password,
 * and a button to register the new user.
 */
class SignUpActivity : AppCompatActivity() {

    // Firebase Authentication instance to handle user registration.
    private lateinit var auth: FirebaseAuth
    // Firebase Realtime Database instance to store user data.
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set the content view to the sign-up activity layout.
        setContentView(R.layout.activity_sign_up)

        // Initialize the Firebase Authentication and Realtime Database instances.
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        // Get references to the UI elements from the layout.
        val etName = findViewById<EditText>(R.id.et_name)
        val etAddress = findViewById<EditText>(R.id.et_address)
        val etPhone = findViewById<EditText>(R.id.et_phone)
        val etEmail = findViewById<EditText>(R.id.et_email)
        val etPassword = findViewById<EditText>(R.id.et_password)
        val btnRegister = findViewById<Button>(R.id.btn_register)

        // Set a click listener on the register button.
        btnRegister.setOnClickListener {
            // Get the user's information from the EditText fields.
            val name = etName.text.toString().trim()
            val address = etAddress.text.toString().trim()
            val phone = etPhone.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            // Validate that all fields are filled.
            if (name.isEmpty() || address.isEmpty() || phone.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Attempt to create a new user with the provided email and password.
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // If user creation is successful, get the new user's UID.
                        val firebaseUser = auth.currentUser
                        val uid = firebaseUser!!.uid
                        // Create a User object with the user's information.
                        val user = User(name, address, phone)

                        // Save the user's information to the Firebase Realtime Database.
                        database.reference.child("users").child(uid).setValue(user)

                        // Display a success message and navigate to the LandingActivity.
                        Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, LandingActivity::class.java)
                        startActivity(intent)
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                        finish()
                    } else {
                        // If user creation fails, log the error and display a message to the user.
                        Log.w("SignUpActivity", "createUserWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Authentication failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }
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