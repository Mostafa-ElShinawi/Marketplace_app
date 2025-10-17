package com.example.ms1

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

/**
 * This activity serves as the user's main screen after a successful login.
 * It displays the user's profile information (name, email, phone, address),
 * allows the user to edit and save their information, and provides a logout button.
 */
class LandingActivity : AppCompatActivity() {

    // Firebase Authentication and Realtime Database instances.
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    // UI elements for displaying and editing user information.
    private lateinit var etName: EditText
    private lateinit var tvEmail: TextView
    private lateinit var etPhone: EditText
    private lateinit var etAddress: EditText
    private lateinit var btnEdit: Button
    private lateinit var btnSave: Button
    private lateinit var btnLogout: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set the content view to the landing activity layout.
        setContentView(R.layout.activity_landing)

        // Initialize Firebase instances.
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        // Get references to the UI elements from the layout.
        etName = findViewById(R.id.et_name)
        tvEmail = findViewById(R.id.tv_email)
        etPhone = findViewById(R.id.et_phone)
        etAddress = findViewById(R.id.et_address)
        btnEdit = findViewById(R.id.btn_edit)
        btnSave = findViewById(R.id.btn_save)
        btnLogout = findViewById(R.id.btn_logout)

        // Initially, the input fields are disabled to prevent accidental editing.
        setFieldsEnabled(false)

        // Get the currently signed-in user.
        val user = auth.currentUser

        // If no user is signed in, redirect to the LoginActivity.
        if (user == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            finish()
        } else {
            // If a user is signed in, display their email.
            tvEmail.text = user.email
            // Get the user's unique ID.
            val uid = user.uid
            // Fetch the user's additional information from the Realtime Database.
            database.reference.child("users").child(uid).get().addOnSuccessListener {
                if (it.exists()){
                    // If the user's data exists, deserialize it into a User object.
                    val userData = it.getValue(User::class.java)
                    // Populate the UI fields with the user's data.
                    etName.setText(userData?.name)
                    etPhone.setText(userData?.phone)
                    etAddress.setText(userData?.address)
                }else{
                    Toast.makeText(this,"User Not Found",Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener{
                Toast.makeText(this,"Failed to get user info",Toast.LENGTH_SHORT).show()
            }
        }

        // Set a click listener on the edit button.
        btnEdit.setOnClickListener {
            // Enable the input fields for editing.
            setFieldsEnabled(true)
            // Hide the edit button and show the save button.
            btnEdit.visibility = View.GONE
            btnSave.visibility = View.VISIBLE
        }

        // Set a click listener on the save button.
        btnSave.setOnClickListener {
            // Get the updated information from the input fields.
            val name = etName.text.toString().trim()
            val phone = etPhone.text.toString().trim()
            val address = etAddress.text.toString().trim()

            // Validate that the fields are not empty.
            if (name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Get the current user's UID.
            val currentUser = auth.currentUser
            val uid = currentUser!!.uid
            // Create a new User object with the updated information.
            val updatedUser = User(name, address, phone)

            // Save the updated user object to the Realtime Database.
            database.reference.child("users").child(uid).setValue(updatedUser)
                .addOnSuccessListener {
                    // On success, show a confirmation message.
                    Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                    // Disable the input fields again.
                    setFieldsEnabled(false)
                    // Show the edit button and hide the save button.
                    btnEdit.visibility = View.VISIBLE
                    btnSave.visibility = View.GONE
                }
                .addOnFailureListener {
                    // On failure, show an error message.
                    Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show()
                }
        }

        // Set a click listener on the logout button.
        btnLogout.setOnClickListener {
            // Sign the user out of Firebase Authentication.
            auth.signOut()
            // Redirect the user to the LoginActivity.
            startActivity(Intent(this, LoginActivity::class.java))
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            finish()
        }
    }

    /**
     * A helper function to enable or disable the user profile input fields.
     * @param enabled A boolean to set the enabled state of the EditText fields.
     */
    private fun setFieldsEnabled(enabled: Boolean) {
        etName.isEnabled = enabled
        etPhone.isEnabled = enabled
        etAddress.isEnabled = enabled
    }

    /**
     * Overrides the default finish method to add a custom transition animation.
     */
    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }
}