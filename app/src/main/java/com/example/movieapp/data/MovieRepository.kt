package com.example.movieapp.data

import com.example.movieapp.data.local.MovieDao
import com.example.movieapp.data.local.sharedpreferences.SharedPrefsHelper
import com.example.movieapp.data.remote.MovieRemoteDataSource
import com.example.movieapp.model.Movie
import com.example.movieapp.model.MovieDesc
import com.example.movieapp.model.Result
import com.example.movieapp.model.TrendingMovieResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject


class MovieRepository @Inject constructor(
    private val movieRemoteDataSource: MovieRemoteDataSource,
    private val movieDao: MovieDao,
    val sharedPrefsHelper: SharedPrefsHelper
)
{

    private fun checkValidateData():Boolean{
        return System.currentTimeMillis()<(sharedPrefsHelper.get<Long>(SharedPrefsHelper.LAST_UPDATE,0)+SharedPrefsHelper.MILLIS_UNTIL_PROMPT)
    }

    suspend fun fetching(): Flow<Result<TrendingMovieResponse>?> {
        return flow {
            val isDataInRoom= getAllMovies().isNotEmpty()

            if (checkValidateData()&&isDataInRoom){
                val result = fetchTrendingMoviesCached()
                emit(result)

                if (result != null) {
                    result.data?.results?.let { it ->
                        movieDao.insertAll(it)
                    }
                }
            }else{

                clearAllMovies()
                emit(Result.loading())
                val result = movieRemoteDataSource.fetchTrendingMovies()
                //Cache to database if response is successful
                if (result.status == Result.Status.SUCCESS) {

                    result.data?.results?.let { it ->
                        saveAllData(it)
                    }
                }
                emit(result)
            }
        }.flowOn(Dispatchers.IO)
    }


    private suspend fun saveAllData(movies: List<Movie>){
        insertAllMovies(movies)
        val current=System.currentTimeMillis()
        sharedPrefsHelper.save(SharedPrefsHelper.LAST_UPDATE,System.currentTimeMillis())
    }


     suspend fun clearAllMovies(){
        movieDao.deleteAllMovies()
    }

    suspend fun insertAllMovies(movies :List<Movie>){
        movieDao.insertAll(movies)
    }


     suspend fun fetchTrendingMoviesCached(): Result<TrendingMovieResponse>? =
        movieDao.getAll()?.let {
            Result.success(TrendingMovieResponse(it))
        }

    suspend fun getAllMovies():List<Movie>{
        return movieDao.getAll()
    }

    private fun fetchMovieCached(id: Int): Result<MovieDesc>? =
        movieDao.getMovieById(id)?.let {
            Result.success(it)
        }

    suspend fun fetchMovie(id: Int): Flow<Result<MovieDesc>?> {
        return flow {
            emit(fetchMovieCached(id))
            emit(Result.loading())
            emit(movieRemoteDataSource.fetchMovie(id))
        }.flowOn(Dispatchers.IO)
    }

}