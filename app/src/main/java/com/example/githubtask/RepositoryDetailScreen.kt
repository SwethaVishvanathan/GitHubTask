package com.example.githubtask
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepositoryDetailScreen(
    navController: NavController,
    repository: Repository,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Repository Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = rememberImagePainter(repository.owner.avatar_url),
                contentDescription = "Owner Avatar",
                modifier = Modifier
                    .size(80.dp)
                    .padding(bottom = 16.dp)
            )
            Text(
                text = repository.name,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Owner: ${repository.owner.login}",
                fontSize = 16.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            repository.description?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                    repository.language?.let { language ->
                    Icon(
                        painter = painterResource(id = R.drawable.ic_language),
                        contentDescription = "Language Icon",
                        tint = Color.Gray,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = language, color = Color.Gray)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Icon(
                    painter = painterResource(id = R.drawable.ic_star),
                    contentDescription = "Star Icon",
                    tint = Color.Yellow,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "${repository.stargazers_count}", color = Color.Gray)
            }
        }
    }
}
