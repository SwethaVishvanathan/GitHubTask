package com.example.githubtask
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepositoryListScreen(
    navController: NavController,
    viewModel: SignInViewModel,
    repositories: List<Repository>,
    onSearch: (String) -> Unit,
    isLoading: Boolean // Add loading state
) {
    var searchQuery by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }

    Column {
        TopAppBar(
            title = { Text("GitHub Repository") },
            actions = {
                IconButton(onClick = {
                    // Request focus when the search icon is clicked
                    focusRequester.requestFocus()
                }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search"
                    )
                }
            }
        )

        // Search TextField
        TextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
                onSearch(it) // Call the provided search function
            },
            placeholder = { Text("Search repositories") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .focusRequester(focusRequester) // Attach FocusRequester
        )

        // Show loading indicator
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            // Repository List
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                if (repositories.isEmpty()) {
                    item {
                        Text(
                            text = "No repositories found.",
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                } else {
                    items(repositories) { repo ->
                        RepositoryItem(
                            repo = repo,
                            onClick = {
                                navController.navigate("repositoryDetails/${repo.id}")
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RepositoryItem(repo: Repository, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Display repository owner avatar
            Image(
                painter = rememberImagePainter(data = repo.owner.avatar_url),
                contentDescription = "Owner Avatar",
                modifier = Modifier.size(40.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                // Display repository name
                Text(
                    text = repo.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

                // Display repository description if available
                repo.description?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 2
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Display repository language and stars
                Row(verticalAlignment = Alignment.CenterVertically) {
                    repo.language?.let { language ->
                        Icon(
                            painter = painterResource(id = R.drawable.ic_language),
                            contentDescription = "Language Icon",
                            tint = Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = language, color = Color.Gray)
                    }
                    Spacer(modifier = Modifier.width(16.dp))

                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Star Icon",
                        tint = Color.Yellow,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "${repo.stargazers_count}", color = Color.Gray)
                }
            }
        }
    }
}
