package com.example.movieapp.ui.details

import androidx.lifecycle.*
import com.example.movieapp.data.MovieRepository
import com.example.movieapp.model.MovieDesc
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.movieapp.model.Result
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel  @Inject constructor(private val movieRepository: MovieRepository) : ViewModel() {

    private var _id = MutableLiveData<Int>()
    private val _movie: LiveData<Result<MovieDesc>> = _id.distinctUntilChanged().switchMap {
        liveData {
            movieRepository.fetchMovie(it).onStart {
                emit(Result.loading())
            }.collect {
                it?.let { it1 -> emit(it1) }
            }
        }
    }
    val movie = _movie

    fun getMovieDetail(id: Int) {
        _id.value = id
    }
}