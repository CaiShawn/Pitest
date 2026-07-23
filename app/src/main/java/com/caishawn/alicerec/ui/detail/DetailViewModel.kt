package com.caishawn.alicerec.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caishawn.alicerec.data.repository.MovieRepository
import com.caishawn.alicerec.domain.model.Movie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class DetailUiState(
    val movie: Movie? = null,
    val isSaved: Boolean = false
)

class DetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: MovieRepository
) : ViewModel() {

    private val movieId: Int = savedStateHandle["movieId"] ?: -1

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val movie = repository.getMovieById(movieId)
            _uiState.value = _uiState.value.copy(
                movie = movie,
                isSaved = movie != null
            )
        }
    }

    fun addMovie(movie: Movie, status: String) {
        viewModelScope.launch {
            repository.addMovie(movie, status)
            _uiState.value = _uiState.value.copy(isSaved = true)
        }
    }

    fun updateStatus(status: String) {
        viewModelScope.launch {
            repository.updateStatus(movieId, status)
        }
    }

    fun deleteMovie() {
        viewModelScope.launch {
            repository.deleteMovie(movieId)
            _uiState.value = _uiState.value.copy(isSaved = false)
        }
    }
}
