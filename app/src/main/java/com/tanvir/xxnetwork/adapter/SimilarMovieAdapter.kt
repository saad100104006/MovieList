package com.tanvir.xxnetwork.adapter


import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.tanvir.xxnetwork.Utils.Constants
import com.tanvir.xxnetwork.databinding.HomeItemBinding
import com.tanvir.xxnetwork.model.Movie
import com.tanvir.xxnetwork.ui.fragments.HomeFragmentDirections
import com.tanvir.xxnetwork.ui.fragments.MovieDetailsFragmentDirections
import com.tanvir.xxnetwork.ui.fragments.MovieDetailsFragmentDirections.actionMovieDetailsSelf
import java.util.*

class SimilarMovieAdapter(var context: Context, var moviesList: List<Movie>) : RecyclerView.Adapter<SimilarMovieAdapter.SimilarViewHolder>() {
    lateinit var binding: HomeItemBinding

    fun setMoviesList(moviesList: ArrayList<Movie>) {
        this.moviesList = moviesList
        notifyDataSetChanged()
    }

    class SimilarViewHolder(binding: HomeItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val binding: HomeItemBinding = binding
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimilarViewHolder {
        val inflater = LayoutInflater.from(context)
        binding = HomeItemBinding.inflate(inflater, parent, false)
        return SimilarViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SimilarViewHolder, position: Int) {
        holder.binding.movieItemRelativeLayout.clipToOutline = true
        holder.binding.movieItemName.text = moviesList[position].title

        Glide.with(context).load(Constants.ImageBaseURLw500 + moviesList[position].poster_path)
                .into(holder.binding.movieItemImage)

        holder.binding.movieItemRelativeLayout.setOnClickListener { view ->
            val action: MovieDetailsFragmentDirections.ActionMovieDetailsSelf = MovieDetailsFragmentDirections
                    .actionMovieDetailsSelf(moviesList[position].id)
            Navigation.findNavController(view).navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return if(moviesList.size > Constants.MAX_ITEM){
            Constants.MAX_ITEM
        } else {
            moviesList.size;
        }
    }
}