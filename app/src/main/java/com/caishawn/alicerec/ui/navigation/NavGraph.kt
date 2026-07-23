package com.caishawn.alicerec.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.caishawn.alicerec.ui.collection.CollectionScreen
import com.caishawn.alicerec.ui.collection.CollectionViewModel
import com.caishawn.alicerec.ui.detail.DetailScreen
import com.caishawn.alicerec.ui.detail.DetailViewModel
import com.caishawn.alicerec.ui.search.SearchScreen
import com.caishawn.alicerec.ui.search.SearchViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

object Routes {
    const val SEARCH = "search"
    const val COLLECTION = "collection"
    const val DETAIL = "detail/{movieId}"

    fun detail(movieId: Int) = "detail/$movieId"
}

@Composable
fun NavGraph(
    navController: NavHostController,
    paddingValues: PaddingValues = PaddingValues(0.dp)
) {
    Box(modifier = Modifier.padding(paddingValues)) {
        NavHost(
            navController = navController,
            startDestination = Routes.COLLECTION
        ) {
        composable(Routes.COLLECTION) {
            val vm: CollectionViewModel = koinViewModel()
            val uiState by vm.uiState.collectAsState()

            CollectionScreen(
                uiState = uiState,
                onMovieClick = { movie ->
                    navController.navigate(Routes.detail(movie.id))
                },
                onMarkWatched = vm::markAsWatched,
                onMarkWantToSee = vm::markAsWantToSee,
                onDelete = vm::deleteMovie
            )
        }

        composable(Routes.SEARCH) {
            val vm: SearchViewModel = koinViewModel()
            val uiState by vm.uiState.collectAsState()

            SearchScreen(
                uiState = uiState,
                onQueryChanged = vm::onQueryChanged,
                onMovieClick = { movie ->
                    navController.navigate(Routes.detail(movie.id))
                },
                onAddMovie = vm::addMovie
            )
        }

        composable(
            route = Routes.DETAIL,
            arguments = listOf(
                navArgument("movieId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getInt("movieId") ?: return@composable
            val vm: DetailViewModel = koinViewModel { parametersOf(movieId) }

            val uiState by vm.uiState.collectAsState()

            DetailScreen(
                uiState = uiState,
                onBack = { navController.popBackStack() },
                onAdd = { movie, status -> vm.addMovie(movie, status) },
                onUpdateStatus = vm::updateStatus,
                onDelete = {
                    vm.deleteMovie()
                    navController.popBackStack()
                }
            )
        }
        }
    }
}
