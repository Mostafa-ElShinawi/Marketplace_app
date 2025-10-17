package com.example.ms1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * The main entry point of the application after the splash screen.
 * This activity is currently a placeholder and simply displays the `activity_main` layout.
 * The initial routing logic is handled by [SplashActivity].
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Sets the content view to the main activity layout.
        setContentView(R.layout.activity_main)
    }
}