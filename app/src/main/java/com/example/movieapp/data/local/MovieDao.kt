package com.example.movieapp.data.local

import androidx.room.*
import com.example.movieapp.model.Movie
import com.example.movieapp.model.MovieDesc

@Dao
interface MovieDao {

    @Query("SELECT * FROM movie order by popularity DESC")
    fun getAll(): List<Movie>

    @Query("SELECT * FROM movie WHERE id = :id")
    fun getMovieById(id : Int): MovieDesc?


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(movies: List<Movie>)

    @Delete
    fun delete(movie: Movie)

    @Delete
    fun deleteAll(movie: List<Movie>)

    @Query("DELETE  FROM Movie")
    suspend fun deleteAllMovies()
}