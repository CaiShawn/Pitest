package com.caishawn.alicerec.di

import androidx.room.Room
import com.caishawn.alicerec.BuildConfig
import com.caishawn.alicerec.data.local.AppDatabase
import com.caishawn.alicerec.data.remote.TmdbApi
import com.caishawn.alicerec.data.repository.MovieRepository
import com.caishawn.alicerec.ui.collection.CollectionViewModel
import com.caishawn.alicerec.ui.detail.DetailViewModel
import com.caishawn.alicerec.ui.search.SearchViewModel
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val appModule = module {

    // ── Database ──────────────────────────────────────────

    single {
        Room.databaseBuilder(
            androidApplication(),
            AppDatabase::class.java,
            "alicerec.db"
        ).build()
    }

    single { get<AppDatabase>().movieDao() }

    // ── Network ───────────────────────────────────────────

    single {
        val authInterceptor = Interceptor { chain ->
            val original = chain.request()
            val url = original.url.newBuilder()
                .addQueryParameter("api_key", BuildConfig.TMDB_API_KEY)
                .build()
            chain.proceed(original.newBuilder().url(url).build())
        }

        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }

        OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(logging)
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .client(get())
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(TmdbApi::class.java)
    }

    // ── Repository ────────────────────────────────────────

    single { MovieRepository(get(), get()) }

    // ── ViewModels ────────────────────────────────────────

    viewModel { SearchViewModel(get()) }
    viewModel { CollectionViewModel(get()) }
    viewModel { (movieId: Int) -> DetailViewModel(movieId, get()) }
}
