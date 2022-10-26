package com.example.movieapp.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.data.MovieRepository
import com.example.movieapp.data.local.sharedpreferences.SharedPrefsHelper
import com.example.movieapp.model.Movie
import com.example.movieapp.model.TrendingMovieResponse
import com.example.movieapp.model.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import java.lang.Exception
import java.net.ConnectException
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val movieRepository: MovieRepository,val sharedPrefsHelper: SharedPrefsHelper) :
    ViewModel() {

    private val _movieList = MutableLiveData<Result<TrendingMovieResponse>>()
    val movieList = _movieList

    init {
        fetchMovies()
    }


    private fun fetchMovies() {
        viewModelScope.launch {
            movieRepository.fetching().collect {
                _movieList.value = it
            }
        }
    }


}