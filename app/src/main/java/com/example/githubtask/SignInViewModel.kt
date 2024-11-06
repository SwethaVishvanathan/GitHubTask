package com.example.githubtask

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignInViewModel(
    private val gitHubService: GitHubService,
    private val repositoryDao: RepositoryDao,
    private val username: String
) : ViewModel() {
    private val _state = MutableStateFlow(SignInState())
    val state: StateFlow<SignInState> = _state.asStateFlow()
    private val _repositories = MutableStateFlow<List<Repository>>(emptyList())
    val repositories: StateFlow<List<Repository>> = _repositories.asStateFlow()
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()
    init {
        fetchRepositories()
    }
    fun onSignInResult(isSuccessful: Boolean, errorMessage: String? = null) {
        _state.update {
            it.copy(
                isSignInSuccessful = isSuccessful,
                signInError = if (!isSuccessful) errorMessage ?: "Sign-in failed" else null
            )
        }

        // Navigate to repository list on success
        if (isSuccessful) {
            _state.value = _state.value.copy(isSignInSuccessful = true)
        }
    }
    fun searchRepositories(query: String) {
        viewModelScope.launch {
            _loading.value = true // Set loading to true
            try {
                val response = gitHubService.searchRepositories(query)
                _repositories.value = response.items
                // Cache results for offline access
                repositoryDao.insertRepositories(response.items)
            }
            finally {
                _loading.value = false // Set loading to false
            }
        }
    }

    fun resetState() {
        _state.update { SignInState() }
    }

    private fun fetchRepositories() {
        viewModelScope.launch {
            _loading.value = true // Set loading to true while fetching
            try {
                val apiRepositories = gitHubService.listRepositories(username)
                _repositories.value = apiRepositories
                cacheRepositories(apiRepositories)
            } catch (e: Exception) {
                _repositories.value = loadCachedRepositories()
            } finally {
                _loading.value = false // Set loading to false after fetching
            }
        }
    }
    fun fetchFcmToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                // Handle the token (e.g., send it to your backend)
                Log.d("FCM", "FCM Token: $token")
            } else {
                Log.w("FCM", "Fetching FCM token failed", task.exception)
            }
        }
    }
    private suspend fun cacheRepositories(repositories: List<Repository>) {
        withContext(Dispatchers.IO) {
            repositoryDao.insertRepositories(repositories)
        }
    }

    private suspend fun loadCachedRepositories(): List<Repository> {
        return withContext(Dispatchers.IO) {
            repositoryDao.getAllRepositories()
        }
    }
}
