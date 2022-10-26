package com.example.movieapp.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.movieapp.Config
import com.example.movieapp.R
import com.example.movieapp.databinding.ListItemMovieBinding
import com.example.movieapp.model.Movie
import com.example.movieapp.ui.details.DetailsFragment

class MoviesAdapter (private val context: Context, private val list: ArrayList<Movie>) :
    RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>() {

    class MovieViewHolder(private val context: Context, itemView: ListItemMovieBinding) : RecyclerView.ViewHolder(itemView.root) {
        val binding = ListItemMovieBinding.bind(itemView.root)

        fun bind(movie: Movie) {
            itemView.setOnClickListener {
                val bundle = bundleOf(DetailsFragment.EXTRAS_MOVIE_ID to movie.id)
                itemView.findNavController().navigate(R.id.action_mainFragment_to_detailsFragment,bundle)

            }
            binding.tvTitle.text = movie.title
            Glide.with(context).load(Config.IMAGE_URL + movie.poster_path)
                .apply(RequestOptions().override(400, 400).centerInside()).into(binding.ivPoster)

        }

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {

        val view = ListItemMovieBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(context, view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(list[position])
    }

    fun updateData(newList: List<Movie>) {
        list.clear()
        val sortedList = newList.sortedBy { movie -> movie.popularity }
        list.addAll(sortedList)
        notifyDataSetChanged()
    }




}