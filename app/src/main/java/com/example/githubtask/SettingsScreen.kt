package com.example.githubtask

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    userData: UserData?,
    onSignOutClick: () -> Unit,
    onSettingsClick: () -> Unit// Add this parameter
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings", style = MaterialTheme.typography.headlineLarge) },
                actions = {
                    IconButton(onClick = onSettingsClick) {
                        Icon(painter = painterResource(id = R.drawable.ic_settings), contentDescription = "Settings")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(onClick = onSignOutClick) {
                Text("Sign out")
            }
        }
    }
}