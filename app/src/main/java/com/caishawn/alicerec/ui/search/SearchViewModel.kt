package com.caishawn.alicerec.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caishawn.alicerec.data.repository.MovieRepository
import com.caishawn.alicerec.domain.model.Movie
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class SearchUiState(
    val query: String = "",
    val results: List<Movie> = emptyList(),
    val popular: List<Movie> = emptyList(),
    val isLoading: Boolean = false,
    val hasSearched: Boolean = false
)

class SearchViewModel(
    private val repository: MovieRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    init {
        loadPopular()
    }

    private fun loadPopular() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(popular = repository.getPopularMovies())
        }
    }

    fun onQueryChanged(query: String) {
        _uiState.value = _uiState.value.copy(query = query)

        searchJob?.cancel()
        if (query.isBlank()) {
            _uiState.value = _uiState.value.copy(results = emptyList(), hasSearched = false)
            return
        }

        searchJob = viewModelScope.launch {
            delay(500) // debounce
            _uiState.value = _uiState.value.copy(isLoading = true, hasSearched = true)
            val results = repository.searchMovies(query)
            _uiState.value = _uiState.value.copy(results = results, isLoading = false)
        }
    }

    fun addMovie(movie: Movie, status: String) {
        viewModelScope.launch {
            repository.addMovie(movie, status)
        }
    }
}
