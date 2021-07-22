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
import com.tanvir.xxnetwork.db.FavoriteMovie
import com.tanvir.xxnetwork.ui.fragments.FavoriteFragmentDirections

class FavoriteAdapter(var context: Context, var moviesList: List<FavoriteMovie>) : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    lateinit var binding: HomeItemBinding

    fun setMovieList(moviesList: List<FavoriteMovie>) {
        this.moviesList = moviesList
        notifyDataSetChanged()
    }

    class FavoriteViewHolder(binding: HomeItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val binding: HomeItemBinding = binding
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val inflater = LayoutInflater.from(context)
        binding = HomeItemBinding.inflate(inflater, parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.binding.movieItemRelativeLayout.clipToOutline = true
        holder.binding.movieItemName.text = moviesList[position].title

        Glide.with(context).load(Constants.ImageBaseURLw500 + moviesList[position].poster_path)
            .into(holder.binding.movieItemImage)
        holder.binding.movieItemRelativeLayout.setOnClickListener { view ->
            val action: FavoriteFragmentDirections.ActionFavoriteToMovieDetails =
                FavoriteFragmentDirections.actionFavoriteToMovieDetails(
                    moviesList[position].id
                )
            Navigation.findNavController(view).navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return moviesList.size
    }


}