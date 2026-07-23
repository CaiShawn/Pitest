package com.caishawn.alicerec.domain.model

data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    val posterPath: String?,
    val backdropPath: String?,
    val releaseDate: String,
    val voteAverage: Double,
    val status: String = "want_to_see",
    val addedAt: Long = System.currentTimeMillis()
) {
    val posterUrl: String?
        get() = posterPath?.let { "https://image.tmdb.org/t/p/w500$it" }

    val backdropUrl: String?
        get() = backdropPath?.let { "https://image.tmdb.org/t/p/w780$it" }

    val year: String
        get() = releaseDate.take(4)

    val displayRating: String
        get() = String.format("%.1f", voteAverage)
}
