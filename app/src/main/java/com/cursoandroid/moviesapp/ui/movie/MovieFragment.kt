package com.cursoandroid.moviesapp.ui.movie

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import com.cursoandroid.moviesapp.R
import com.cursoandroid.moviesapp.core.Resource
import com.cursoandroid.moviesapp.data.local.AppDatabase
import com.cursoandroid.moviesapp.data.local.LocalMovieDataSource
import com.cursoandroid.moviesapp.data.model.Movie
import com.cursoandroid.moviesapp.data.remote.RemoteMovieDataSource
import com.cursoandroid.moviesapp.databinding.FragmentMovieBinding
import com.cursoandroid.moviesapp.presentation.MovieViewModel
import com.cursoandroid.moviesapp.presentation.MovieViewModelFactory
import com.cursoandroid.moviesapp.repository.MovieRepositoryImpl
import com.cursoandroid.moviesapp.repository.RetrofitClient
import com.cursoandroid.moviesapp.ui.movie.adapters.MovieAdapter
import com.cursoandroid.moviesapp.ui.movie.adapters.concat.PopularConcatAdapter
import com.cursoandroid.moviesapp.ui.movie.adapters.concat.TopRatedConcatAdapter
import com.cursoandroid.moviesapp.ui.movie.adapters.concat.UpcomingConcatAdapter


class MovieFragment : Fragment(R.layout.fragment_movie), MovieAdapter.OnMovieClickListener {

    private lateinit var binding: FragmentMovieBinding
    private val viewModel by viewModels<MovieViewModel> {MovieViewModelFactory(MovieRepositoryImpl(
        RemoteMovieDataSource(RetrofitClient.webservice),
        LocalMovieDataSource(AppDatabase.getDatabase(requireContext()).movieDao())
    )
    )
    }

    private lateinit var concatAdapter : ConcatAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMovieBinding.bind(view)

        concatAdapter = ConcatAdapter()

        viewModel.fetchMainScreenMovies().observe(viewLifecycleOwner, Observer { result ->
            when(result){
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    concatAdapter.apply {
                        addAdapter(0, UpcomingConcatAdapter(MovieAdapter(result.data.first.results, this@MovieFragment)))
                        addAdapter(1, TopRatedConcatAdapter(MovieAdapter(result.data.second.results, this@MovieFragment)))
                        addAdapter(2, PopularConcatAdapter(MovieAdapter(result.data.third.results, this@MovieFragment)))
                    }

                    binding.rvMovies.adapter = concatAdapter
                }
                is Resource.Failure -> {
                    Log.d("Error", "${result.exception}")
                }
            }
        })

    }

    override fun onMovieClick(movie: Movie) {
        val action = MovieFragmentDirections.actionMovieFragmentToMovieDetailFragment(
            movie.poster_path,
            movie.backdrop_path,
            movie.vote_average.toFloat(),
            movie.vote_count,
            movie.overview,
            movie.title,
            movie.original_language,
            movie.release_date)
        findNavController().navigate(action)
    }
}