package com.cursoandroid.moviesapp.data.remote

import com.cursoandroid.moviesapp.application.AppConstants
import com.cursoandroid.moviesapp.data.model.MovieList
import com.cursoandroid.moviesapp.repository.WebService

class RemoteMovieDataSource(private val webService: WebService) {

    suspend fun getUpcomingMovies(): MovieList {
        return webService.getUpcomingMovies(AppConstants.API_KEY)
    }

    suspend fun getTopRatedMovies(): MovieList {
        return webService.getTopRatedMovies(AppConstants.API_KEY)
    }

    suspend fun getPopularMovies(): MovieList {
        return webService.getPopularMovies(AppConstants.API_KEY)
    }
}