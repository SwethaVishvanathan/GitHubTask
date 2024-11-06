package com.example.githubtask

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun SignInScreen(
    navController: NavController,
    state: SignInState,
    onSignInClick: () -> Unit
) {
    val context = LocalContext.current
    LaunchedEffect(state.signInError) {
        state.signInError?.let { error ->
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
        }
    }
    Box(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "GitHub Repository",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 32.dp)
            )
            Button(
                onClick = onSignInClick,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
            ) {
                Text(text = "Sign in with Google")
            }
            if (state.isSignInSuccessful) {
                Spacer(modifier = Modifier.height(16.dp))
                CircularProgressIndicator()
            }
        }
    }
}