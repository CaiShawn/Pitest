package com.caishawn.alicerec.data.repository

import com.caishawn.alicerec.data.local.MovieDao
import com.caishawn.alicerec.data.remote.TmdbApi
import com.caishawn.alicerec.data.remote.TmdbMovie
import com.caishawn.alicerec.data.remote.TmdbSearchResponse
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

class MovieRepositoryTest {

    private val tmdbApi: TmdbApi = mockk()
    private val movieDao: MovieDao = mockk()
    private val repository = MovieRepository(tmdbApi, movieDao)

    @Test
    fun `getPopularMovies returns movies from TMDb mapped to domain`() = runTest {
        // Arrange: TMDb API returns one popular movie
        val inception = TmdbMovie(
            id = 27205,
            title = "Inception",
            overview = "A thief who steals corporate secrets through dream-sharing.",
            posterPath = "/abc.jpg",
            backdropPath = "/xyz.jpg",
            releaseDate = "2010-07-15",
            voteAverage = 8.4
        )
        coEvery { tmdbApi.getPopular() } returns TmdbSearchResponse(
            page = 1,
            results = listOf(inception),
            totalResults = 1,
            totalPages = 1
        )

        // Act: user asks for popular movies
        val result = repository.getPopularMovies()

        // Assert: returns the movie as a domain Movie, fully translated
        assertEquals(1, result.size)
        assertEquals(27205, result[0].id)
        assertEquals("Inception", result[0].title)
        assertEquals("/abc.jpg", result[0].posterPath)
        assertEquals(8.4, result[0].voteAverage)
        assertEquals("https://image.tmdb.org/t/p/w500/abc.jpg", result[0].posterUrl)
    }
}