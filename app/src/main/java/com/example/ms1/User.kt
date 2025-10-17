package com.example.ms1

/**
 * A data class to represent a user's profile information.
 * This class is used to store and retrieve user data from the Firebase Realtime Database.
 *
 * @property name The user's full name.
 * @property address The user's address.
 * @property phone The user's phone number.
 */
data class User(
    val name: String? = null,
    val address: String? = null,
    val phone: String? = null
)