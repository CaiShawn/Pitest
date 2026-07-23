package com.caishawn.alicerec.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface TmdbApi {

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("language") language: String = "zh-CN",
        @Query("page") page: Int = 1
    ): TmdbSearchResponse

    @GET("movie/now_playing")
    suspend fun getNowPlaying(
        @Query("language") language: String = "zh-CN",
        @Query("page") page: Int = 1
    ): TmdbSearchResponse

    @GET("movie/popular")
    suspend fun getPopular(
        @Query("language") language: String = "zh-CN",
        @Query("page") page: Int = 1
    ): TmdbSearchResponse
}
