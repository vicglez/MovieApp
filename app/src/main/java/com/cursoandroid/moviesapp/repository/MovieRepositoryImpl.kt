package com.cursoandroid.moviesapp.repository

import com.cursoandroid.moviesapp.data.local.LocalMovieDataSource
import com.cursoandroid.moviesapp.data.model.MovieList
import com.cursoandroid.moviesapp.data.model.toMovieEntity
import com.cursoandroid.moviesapp.data.remote.RemoteMovieDataSource

class MovieRepositoryImpl(
    private val dataSourceRemote: RemoteMovieDataSource,
    private val dataSourceLocal: LocalMovieDataSource
    ): MovieRepository {

    override suspend fun getUpcomingMovies(): MovieList {
        dataSourceRemote.getUpcomingMovies().results.forEach { movie ->
            dataSourceLocal.saveMovie(movie.toMovieEntity("upcoming"))
        }
        return dataSourceLocal.getUpcomingMovies()
    }

    override suspend fun getTopRatedMovies(): MovieList {
        dataSourceRemote.getTopRatedMovies().results.forEach { movie ->
            dataSourceLocal.saveMovie(movie.toMovieEntity("toprated"))
        }
        return dataSourceLocal.getTopRatedMovies()
    }

    override suspend fun getPopularMovies(): MovieList {
        dataSourceRemote.getPopularMovies().results.forEach { movie ->
            dataSourceLocal.saveMovie(movie.toMovieEntity("popular"))
        }
        return dataSourceLocal.getPopularMovies()
    }
}