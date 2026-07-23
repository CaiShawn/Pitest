package com.caishawn.alicerec.ui.collection

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.caishawn.alicerec.domain.model.Movie

@Composable
fun CollectionScreen(
    uiState: CollectionUiState,
    onMovieClick: (Movie) -> Unit,
    onMarkWatched: (Movie) -> Unit,
    onMarkWantToSee: (Movie) -> Unit,
    onDelete: (Movie) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Want to See section
        item {
            SectionHeader(
                title = "想看",
                count = uiState.wantToSee.size
            )
        }

        if (uiState.wantToSee.isEmpty()) {
            item {
                Text(
                    "还没有想看的电影，去搜索添加吧！",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }

        items(uiState.wantToSee, key = { it.id }) { movie ->
            MovieRow(
                movie = movie,
                onClick = { onMovieClick(movie) },
                primaryAction = { onMarkWatched(movie) },
                primaryIcon = Icons.Default.Check,
                primaryLabel = "已看",
                secondaryAction = { onDelete(movie) },
                secondaryIcon = Icons.Default.Delete,
                secondaryLabel = "删除"
            )
        }

        // Watched section
        item {
            SectionHeader(
                title = "已看",
                count = uiState.watched.size
            )
        }

        if (uiState.watched.isEmpty()) {
            item {
                Text(
                    "还没有已看的电影",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }

        items(uiState.watched, key = { it.id }) { movie ->
            MovieRow(
                movie = movie,
                onClick = { onMovieClick(movie) },
                primaryAction = { onMarkWantToSee(movie) },
                primaryIcon = Icons.Default.Refresh,
                primaryLabel = "想看",
                secondaryAction = { onDelete(movie) },
                secondaryIcon = Icons.Default.Delete,
                secondaryLabel = "删除"
            )
        }
    }
}

@Composable
private fun SectionHeader(title: String, count: Int) {
    Text(
        "$title ($count)",
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(vertical = 4.dp)
    )
}

@Composable
private fun MovieRow(
    movie: Movie,
    onClick: () -> Unit,
    primaryAction: () -> Unit,
    primaryIcon: androidx.compose.ui.graphics.vector.ImageVector,
    primaryLabel: String,
    secondaryAction: () -> Unit,
    secondaryIcon: androidx.compose.ui.graphics.vector.ImageVector,
    secondaryLabel: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = movie.posterUrl,
                contentDescription = movie.title,
                modifier = Modifier
                    .width(60.dp)
                    .height(90.dp)
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    movie.title,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    "${movie.year}  ⭐ ${movie.displayRating}",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            IconButton(onClick = primaryAction) {
                Icon(primaryIcon, contentDescription = primaryLabel)
            }
            IconButton(onClick = secondaryAction) {
                Icon(secondaryIcon, contentDescription = secondaryLabel)
            }
        }
    }
}
