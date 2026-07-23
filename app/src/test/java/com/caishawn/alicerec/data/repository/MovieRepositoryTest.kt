package com.caishawn.alicerec.data.repository

import app.cash.turbine.test
import com.caishawn.alicerec.data.local.MovieDao
import com.caishawn.alicerec.data.local.MovieEntity
import com.caishawn.alicerec.data.remote.TmdbApi
import com.caishawn.alicerec.data.remote.TmdbMovie
import com.caishawn.alicerec.data.remote.TmdbSearchResponse
import com.caishawn.alicerec.domain.model.Movie
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

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
        assertEquals(8.4, result[0].voteAverage, 0.0001)
        assertEquals("https://image.tmdb.org/t/p/w500/abc.jpg", result[0].posterUrl)
    }

    @Test
    fun `addMovie persists movie with the given status via DAO`() = runTest {
        // Arrange: a movie the user wants to save
        val movie = Movie(
            id = 27205,
            title = "Inception",
            overview = "A thief who steals corporate secrets through dream-sharing.",
            posterPath = "/abc.jpg",
            backdropPath = "/xyz.jpg",
            releaseDate = "2010-07-15",
            voteAverage = 8.4
        )

        val capturedEntity = slot<MovieEntity>()
        coEvery { movieDao.upsertMovie(capture(capturedEntity)) } returns Unit

        // Act: user adds the movie to their "want to see" list
        repository.addMovie(movie, "want_to_see")

        // Assert: the DAO received an entity with the right id, title, and status
        coVerify(exactly = 1) { movieDao.upsertMovie(any()) }
        val stored = capturedEntity.captured
        assertEquals(27205, stored.id)
        assertEquals("Inception", stored.title)
        assertEquals("want_to_see", stored.status)
    }

    @Test
    fun `updateStatus delegates id and new status to DAO`() = runTest {
        // Arrange: stub the DAO so the call returns successfully
        coEvery { movieDao.updateStatus(any(), any()) } returns Unit

        // Act
        repository.updateStatus(id = 27205, status = "watched")

        // Assert: DAO's updateStatus is called exactly once with the new args,
        // not upsertMovie (which would rewrite all fields).
        coVerify(exactly = 1) { movieDao.updateStatus(27205, "watched") }
    }

    @Test
    fun `getMoviesByStatus queries DAO with given status and maps entities to domain`() = runTest {
        // Arrange: DAO Flow emits one movie matching "want_to_see"
        val daoFlow = flowOf(
            listOf(
                MovieEntity(
                    id = 27205,
                    title = "Inception",
                    overview = "Dream thief",
                    posterPath = "/abc.jpg",
                    backdropPath = null,
                    releaseDate = "2010-07-15",
                    voteAverage = 8.4,
                    status = "want_to_see",
                    addedAt = 1000L
                )
            )
        )
        coEvery { movieDao.getMoviesByStatus("want_to_see") } returns daoFlow

        // Act + Assert
        repository.getMoviesByStatus("want_to_see").test {
            val movies = awaitItem()

            // Contract 1: DAO was queried with the right status
            coVerify(exactly = 1) { movieDao.getMoviesByStatus("want_to_see") }

            // Contract 2: returned movies are mapped to domain layer with status preserved
            assertEquals(1, movies.size)
            assertEquals(27205, movies[0].id)
            assertEquals("Inception", movies[0].title)
            assertEquals("want_to_see", movies[0].status)
            assertEquals(8.4, movies[0].voteAverage, 0.0001)

            awaitComplete()
        }
    }
}