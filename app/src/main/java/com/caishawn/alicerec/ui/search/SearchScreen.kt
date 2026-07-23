package com.caishawn.alicerec.ui.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.caishawn.alicerec.domain.model.Movie

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    uiState: SearchUiState,
    onQueryChanged: (String) -> Unit,
    onMovieClick: (Movie) -> Unit,
    onAddMovie: (Movie, String) -> Unit
) {
    var showDialog by remember { mutableStateOf<Movie?>(null) }

    Column(modifier = Modifier.fillMaxSize()) {
        // Search bar
        SearchBar(
            inputField = {
                SearchBarDefaults.InputField(
                    query = uiState.query,
                    onQueryChange = onQueryChanged,
                    onSearch = {},
                    expanded = false,
                    onExpandedChange = {},
                    placeholder = { Text("搜索电影...") }
                )
            },
            expanded = false,
            onExpandedChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {}

        when {
            !uiState.hasSearched && uiState.popular.isNotEmpty() -> {
                Text(
                    "热门电影",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
                MovieList(
                    movies = uiState.popular,
                    onMovieClick = onMovieClick,
                    onAddMovie = { movie -> showDialog = movie }
                )
            }

            uiState.isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            uiState.hasSearched && uiState.results.isEmpty() -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("没有找到结果", style = MaterialTheme.typography.bodyLarge)
                }
            }

            else -> {
                MovieList(
                    movies = uiState.results,
                    onMovieClick = onMovieClick,
                    onAddMovie = { movie -> showDialog = movie }
                )
            }
        }
    }

    // Add-to-collection dialog
    showDialog?.let { movie ->
        AddMovieDialog(
            movie = movie,
            onDismiss = { showDialog = null },
            onConfirm = { status ->
                onAddMovie(movie, status)
                showDialog = null
            }
        )
    }
}

@Composable
private fun MovieList(
    movies: List<Movie>,
    onMovieClick: (Movie) -> Unit,
    onAddMovie: (Movie) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(movies, key = { it.id }) { movie ->
            MovieCard(
                movie = movie,
                onClick = { onMovieClick(movie) },
                onAdd = { onAddMovie(movie) }
            )
        }
    }
}

@Composable
private fun MovieCard(
    movie: Movie,
    onClick: () -> Unit,
    onAdd: () -> Unit
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
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Poster
            AsyncImage(
                model = movie.posterUrl,
                contentDescription = movie.title,
                modifier = Modifier
                    .width(80.dp)
                    .height(120.dp)
            )

            // Info
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
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
                Text(
                    movie.overview,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Add button
            IconButton(onClick = onAdd) {
                Icon(Icons.Default.Add, contentDescription = "添加到收藏")
            }
        }
    }
}

@Composable
private fun AddMovieDialog(
    movie: Movie,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(movie.title) },
        text = { Text("添加到你的收藏？") },
        confirmButton = {
            TextButton(onClick = { onConfirm("want_to_see") }) {
                Text("想看")
            }
        },
        dismissButton = {
            Row {
                TextButton(onClick = { onConfirm("watched") }) {
                    Text("已看")
                }
                TextButton(onClick = onDismiss) {
                    Text("取消")
                }
            }
        }
    )
}
