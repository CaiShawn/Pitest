package com.caishawn.alicerec.data.repository

import com.caishawn.alicerec.data.local.MovieDao
import com.caishawn.alicerec.data.local.MovieEntity
import com.caishawn.alicerec.data.remote.TmdbApi
import com.caishawn.alicerec.data.remote.TmdbMovie
import com.caishawn.alicerec.domain.model.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MovieRepository(
    private val tmdbApi: TmdbApi,
    private val movieDao: MovieDao
) {

    // ── Remote ────────────────────────────────────────────

    suspend fun searchMovies(query: String): List<Movie> {
        val response = tmdbApi.searchMovies(query)
        return response.results.map { it.toDomainModel() }
    }

    suspend fun getPopularMovies(): List<Movie> {
        val response = tmdbApi.getPopular()
        return response.results.map { it.toDomainModel() }
    }

    // ── Local ─────────────────────────────────────────────

    fun getAllMovies(): Flow<List<Movie>> =
        movieDao.getAllMovies().map { entities ->
            entities.map { it.toDomainModel() }
        }

    fun getMoviesByStatus(status: String): Flow<List<Movie>> =
        movieDao.getMoviesByStatus(status).map { entities ->
            entities.map { it.toDomainModel() }
        }

    suspend fun getMovieById(id: Int): Movie? =
        movieDao.getMovieById(id)?.toDomainModel()

    suspend fun addMovie(movie: Movie, status: String = "want_to_see") {
        movieDao.upsertMovie(movie.toEntity(status))
    }

    suspend fun updateStatus(id: Int, status: String) {
        movieDao.updateStatus(id, status)
    }

    suspend fun deleteMovie(id: Int) {
        movieDao.deleteMovie(id)
    }

    suspend fun isMovieSaved(id: Int): Boolean =
        movieDao.getMovieById(id) != null

    // ── Mappers ───────────────────────────────────────────

    private fun TmdbMovie.toDomainModel() = Movie(
        id = id,
        title = title,
        overview = overview,
        posterPath = posterPath,
        backdropPath = backdropPath,
        releaseDate = releaseDate ?: "",
        voteAverage = voteAverage ?: 0.0
    )

    private fun MovieEntity.toDomainModel() = Movie(
        id = id,
        title = title,
        overview = overview,
        posterPath = posterPath,
        backdropPath = backdropPath,
        releaseDate = releaseDate,
        voteAverage = voteAverage,
        status = status,
        addedAt = addedAt
    )

    private fun Movie.toEntity(status: String) = MovieEntity(
        id = id,
        title = title,
        overview = overview,
        posterPath = posterPath,
        backdropPath = backdropPath,
        releaseDate = releaseDate,
        voteAverage = voteAverage,
        status = status
    )
}
