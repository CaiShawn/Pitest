package com.caishawn.alicerec.ui.collection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caishawn.alicerec.data.repository.MovieRepository
import com.caishawn.alicerec.domain.model.Movie
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class CollectionUiState(
    val wantToSee: List<Movie> = emptyList(),
    val watched: List<Movie> = emptyList()
)

class CollectionViewModel(
    private val repository: MovieRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CollectionUiState())
    val uiState: StateFlow<CollectionUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                repository.getMoviesByStatus("want_to_see"),
                repository.getMoviesByStatus("watched")
            ) { wantToSee, watched ->
                CollectionUiState(wantToSee = wantToSee, watched = watched)
            }.collect { state ->
                _uiState.value = state
            }
        }
    }

    fun markAsWatched(movie: Movie) {
        viewModelScope.launch {
            repository.updateStatus(movie.id, "watched")
        }
    }

    fun markAsWantToSee(movie: Movie) {
        viewModelScope.launch {
            repository.updateStatus(movie.id, "want_to_see")
        }
    }

    fun deleteMovie(movie: Movie) {
        viewModelScope.launch {
            repository.deleteMovie(movie.id)
        }
    }
}
