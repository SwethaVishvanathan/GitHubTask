package com.example.githubtask

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SignInViewModelFactory(
    private val githubService: GitHubService,
    private val repositoryDao: RepositoryDao,
    private val username: String
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignInViewModel::class.java)) {
            return SignInViewModel(githubService, repositoryDao,username) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
