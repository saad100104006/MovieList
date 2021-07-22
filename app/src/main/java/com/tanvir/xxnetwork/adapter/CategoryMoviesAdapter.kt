package com.tanvir.xxnetwork.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tanvir.xxnetwork.Utils.Constants
import com.tanvir.xxnetwork.databinding.MovieItemBinding
import com.tanvir.xxnetwork.model.Movie
import com.tanvir.xxnetwork.ui.fragments.MoviesFragmentDirections
import com.tanvir.xxnetwork.ui.fragments.SimilarMoviesFragmentDirections
import java.lang.Exception
import java.util.*

class CategoryMoviesAdapter(var context: Context, var moviesList: ArrayList<Movie>, var isFromSimilarMovies:Boolean) : RecyclerView.Adapter<CategoryMoviesAdapter.CategoryMoviesViewHolder>() {
    lateinit var binding: MovieItemBinding
    lateinit var temp: String

    fun setMovieList(movieList: ArrayList<Movie>) {
        moviesList = movieList
        notifyDataSetChanged()
    }
    class CategoryMoviesViewHolder(binding: MovieItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val binding: MovieItemBinding = binding
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryMoviesViewHolder {
        val inflater = LayoutInflater.from(context)
        binding = MovieItemBinding.inflate(inflater, parent, false)
        return CategoryMoviesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryMoviesViewHolder, position: Int) {
        holder.binding.movieName.text = moviesList[position].title

        temp = ""

        for (i in 0 until moviesList[position].genre_ids.size) {
            if (i == moviesList[position].genre_ids.size - 1) temp += Constants.getGenreMap().get(
                    moviesList[position].genre_ids[i]
            ) else temp += Constants.getGenreMap().get(
                    moviesList[position].genre_ids[i]
            ).toString() + " • "
        }

        holder.binding.movieGenre.text = temp
        try {
            val movieYear: List<String> = moviesList[position].release_date
                    .split("-")
            holder.binding.movieYear.text = movieYear[0]
        } catch (e: Exception) {
            e.printStackTrace()
        }


        Glide.with(context).load(Constants.ImageBaseURL + moviesList[position].poster_path)
                .into(holder.binding.movieImage)

        holder.binding.movieItemLayout.setOnClickListener { view ->

            if(!isFromSimilarMovies) {
                val action: MoviesFragmentDirections.ActionMoviesToMovieDetails =
                        MoviesFragmentDirections.actionMoviesToMovieDetails(
                                moviesList[position].id
                        )

                Navigation.findNavController(view).navigate(action)
            } else {
                val action: SimilarMoviesFragmentDirections.ActionSimilarMoviesToMovieDetails =
                    SimilarMoviesFragmentDirections.actionSimilarMoviesToMovieDetails(
                                moviesList[position].id
                        )

                Navigation.findNavController(view).navigate(action)
            }
        }
        holder.binding.movieItemLayout.clipToOutline = true
    }

    override fun getItemCount(): Int {
        return moviesList.size
    }
}