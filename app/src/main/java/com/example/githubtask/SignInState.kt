package com.example.githubtask

data class SignInState(
    val isSignInSuccessful: Boolean = false,
    val signInError: String? = null,
    val isSignOutSuccessful: Boolean = false,
    val signOutError: String? = null
)
