package com.example.ms1

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

/**
 * This activity serves as a splash screen, which is the first screen the user sees
 * when launching the app. It displays a welcome message and the app's logo.
 * After a short delay, it checks if a user is already signed in and navigates
 * to the appropriate activity ([LandingActivity] if signed in, [LoginActivity] otherwise).
 */
class SplashActivity : AppCompatActivity() {

    // Firebase Authentication instance to check the current user's sign-in status.
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set the content view to the splash screen layout.
        setContentView(R.layout.activity_splash)

        // Initialize the Firebase Authentication instance.
        auth = FirebaseAuth.getInstance()
        // Get the currently signed-in user.
        val currentUser = auth.currentUser

        // A Handler is used to delay the navigation to the next screen.
        // This creates a timed splash screen effect.
        Handler(Looper.getMainLooper()).postDelayed({
            // Check if there is a currently signed-in user.
            if (currentUser != null) {
                // If a user is signed in, navigate to the LandingActivity.
                startActivity(Intent(this, LandingActivity::class.java))
            } else {
                // If no user is signed in, navigate to the LoginActivity.
                startActivity(Intent(this, LoginActivity::class.java))
            }
            // Apply a fade-in/fade-out transition to the new activity.
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            // Finish the SplashActivity so the user cannot navigate back to it.
            finish()
        }, 2000) // The delay is set to 2000 milliseconds (2 seconds).
    }
}