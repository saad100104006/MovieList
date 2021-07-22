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
import java.util.*

class HomeAdapter(var context: Context, var moviesList: List<Movie>) : RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {
    lateinit var binding: HomeItemBinding


    class HomeViewHolder(binding: HomeItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val binding: HomeItemBinding = binding
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val inflater = LayoutInflater.from(context)
        binding = HomeItemBinding.inflate(inflater, parent, false)
        return HomeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.binding.movieItemRelativeLayout.clipToOutline = true
        holder.binding.movieItemName.text = moviesList[position].title

        Glide.with(context).load(Constants.ImageBaseURLw500 + moviesList[position].poster_path)
            .into(holder.binding.movieItemImage)

        holder.binding.movieItemRelativeLayout.setOnClickListener { view ->
            val action: HomeFragmentDirections.ActionHome2ToMovieDetails = HomeFragmentDirections
                .actionHome2ToMovieDetails(moviesList[position].id)
            Navigation.findNavController(view).navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return if(moviesList.size > 7){
            7 } else {
            moviesList.size;
        }
    }
}