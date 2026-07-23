package com.caishawn.alicerec.ui.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.caishawn.alicerec.domain.model.Movie

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    uiState: DetailUiState,
    onBack: () -> Unit,
    onAdd: (Movie, String) -> Unit,
    onUpdateStatus: (String) -> Unit,
    onDelete: () -> Unit
) {
    val movie = uiState.movie

    if (movie == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(movie.title, maxLines = 1) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // Backdrop
            AsyncImage(
                model = movie.backdropUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Title + year
                Text(
                    "${movie.title} (${movie.year})",
                    style = MaterialTheme.typography.headlineSmall
                )

                // Rating
                Text(
                    "⭐ ${movie.displayRating} / 10",
                    style = MaterialTheme.typography.titleMedium
                )

                // Overview
                if (movie.overview.isNotBlank()) {
                    Text(
                        "简介",
                        style = MaterialTheme.typography.titleSmall
                    )
                    Text(
                        movie.overview,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Actions
                if (!uiState.isSaved) {
                    Button(
                        onClick = { onAdd(movie, "want_to_see") },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("想看")
                    }
                    OutlinedButton(
                        onClick = { onAdd(movie, "watched") },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("标记为已看")
                    }
                } else {
                    OutlinedButton(
                        onClick = { onUpdateStatus("watched") },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("标记为已看")
                    }
                    Button(
                        onClick = onDelete,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("从收藏中移除")
                    }
                }
            }
        }
    }
}
