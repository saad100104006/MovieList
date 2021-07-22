package com.tanvir.xxnetwork.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tanvir.xxnetwork.Utils.Constants
import com.tanvir.xxnetwork.databinding.ItemMovieGenreBinding
import com.tanvir.xxnetwork.model.MovieGenre
import com.tanvir.xxnetwork.ui.fragments.HomeFragmentDirections

class GenreMovieAdapter(val context: Context, val movieGenres: ArrayList<MovieGenre>) :
    RecyclerView.Adapter<GenreMovieAdapter.GenreMovieViewHolder>() {
    lateinit var binding: ItemMovieGenreBinding

    class GenreMovieViewHolder(binding: ItemMovieGenreBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val binding: ItemMovieGenreBinding = binding
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreMovieViewHolder {
        val inflater = LayoutInflater.from(context)
        binding = ItemMovieGenreBinding.inflate(inflater, parent, false)
        return GenreMovieAdapter.GenreMovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GenreMovieViewHolder, position: Int) {
        val movieGenre = movieGenres[position]
        holder.binding.genreName.text = movieGenre.genreName
        holder.binding.movieRecycler.layoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        val homeAdapter = HomeAdapter(context, movieGenre.results)
        holder.binding.viewAllCurrent2.setOnClickListener { v ->
            val action: HomeFragmentDirections.ActionHome2ToMovies =
                HomeFragmentDirections.actionHome2ToMovies()
            action.movieCategory = Constants.GENRE
            action.genreName = movieGenre.genreName
            action.genreId = movieGenre.genreId
            Navigation.findNavController(v).navigate(action)
        }
        holder.binding.movieRecycler.adapter = homeAdapter
        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return movieGenres.size
    }
}
