package com.example.githubtask

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.githubtask.ui.theme.GitHubTaskTheme
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    private val googleAuthUiClient by lazy {
        val options = FirebaseOptions.Builder()
            .setApplicationId("1:1035231609428:android:d02531451c302c95a0a3c1")
            .setApiKey("AIzaSyAT2j2Qraqzg2-YaGozrK_aMR2aIdr1uh0")
            .setProjectId("githubtask-e5a8c")
            .build()
        if (FirebaseApp.getApps(this).isEmpty()) {
            FirebaseApp.initializeApp(this)
        }
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val githubService = Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GitHubService::class.java)
        val repositoryDao = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "repository-db"
        ).build().gitHubRepoDao()
        setContent {
            GitHubTaskTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val username = googleAuthUiClient.getSignedInUser()?.username ?: ""
                    val signInViewModelFactory = SignInViewModelFactory(
                        githubService = githubService,
                        repositoryDao = repositoryDao,
                        username = username
                    )
                    val signInViewModel = viewModel<SignInViewModel>(factory = signInViewModelFactory)
                    val onSettingsClick: () -> Unit = {
                        navController.navigate("settings")
                    }
                    signInViewModel.fetchFcmToken()
                    NavHost(navController = navController, startDestination = "repositorylist") {
                        composable("sign_in") {
                            val state by signInViewModel.state.collectAsState()
                            LaunchedEffect(key1 = Unit) {
                                if (googleAuthUiClient.getSignedInUser() != null) {
                                    navController.navigate("repositorylist")
                                }
                            }
                            val launcher = rememberLauncherForActivityResult(
                                contract = ActivityResultContracts.StartIntentSenderForResult(),
                                onResult = { result ->
                                    if (result.resultCode == RESULT_OK) {
                                        lifecycleScope.launch {
                                            val signInResult = googleAuthUiClient.signInWithIntent(
                                                intent = result.data ?: return@launch
                                            )
                                            signInViewModel.onSignInResult(isSuccessful = false)
                                        }
                                    }
                                }
                            )
                            LaunchedEffect(key1 = state.isSignInSuccessful) {
                                if (state.isSignInSuccessful) {
                                    Toast.makeText(
                                        applicationContext,
                                        "Sign in successful",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    navController.navigate("repositorylist")
                                    signInViewModel.resetState()
                                }
                            }
                            SignInScreen(
                                state = state,
                                navController = navController,
                                onSignInClick = {
                                    lifecycleScope.launch {
                                        val signInIntentSender = googleAuthUiClient.signIn()
                                        launcher.launch(
                                            IntentSenderRequest.Builder(
                                                signInIntentSender ?: return@launch
                                            ).build()
                                        )
                                    }
                                }
                            )
                        }
                        composable("repositorylist") {
                            val repositoryList by signInViewModel.repositories.collectAsState()
                            val isLoading by signInViewModel.loading.collectAsState()
                            RepositoryListScreen(
                                repositories = repositoryList,
                                viewModel = signInViewModel,
                                navController = navController,
                                onSearch = { query ->
                                    signInViewModel.searchRepositories(query) // Search function in ViewModel
                                },
                                isLoading = isLoading
                            )
                        }
                        composable("profile") {
                            ProfileScreen(
                                userData = googleAuthUiClient.getSignedInUser(),
                                onSignOut = {
                                    lifecycleScope.launch {
                                        googleAuthUiClient.signOut()
                                        Toast.makeText(
                                            applicationContext,
                                            "Signed out",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        navController.navigate("sign_in") {
                                            popUpTo("sign_in") { inclusive = true }
                                        }
                                    }
                                },
                                onSettingsClick = {
                                    navController.navigate("settings")
                                }
                            )
                        }
                        composable("settings") {
                            SettingsScreen(
                                userData = googleAuthUiClient.getSignedInUser(),
                                onSignOutClick = {
                                    lifecycleScope.launch {
                                        googleAuthUiClient.signOut()
                                        Toast.makeText(
                                            applicationContext,
                                            "Signed out",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        navController.navigate("sign_in") {
                                            popUpTo("sign_in") { inclusive = true }
                                        }
                                    }
                                },
                                onSettingsClick = onSettingsClick
                            )
                        }
                    }
                }
            }
        }
    }
}