package com.tanvir.xxnetwork.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.tanvir.xxnetwork.adapter.FavoriteAdapter
import com.tanvir.xxnetwork.databinding.FavoriteLayoutBinding
import com.tanvir.xxnetwork.db.FavoriteMovie
import com.tanvir.xxnetwork.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : Fragment() {
    lateinit var viewModel: MainViewModel
    lateinit var binding: FavoriteLayoutBinding
    lateinit var adapter: FavoriteAdapter
    var moviesList: List<FavoriteMovie> = mutableListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FavoriteLayoutBinding.inflate(inflater, container, false)
        val view = binding.root
        viewModel = ViewModelProvider(this@FavoriteFragment).get(MainViewModel::class.java)
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        intiRecyclerView()
        observeData()
        binding.clearFavList.setOnClickListener {
            viewModel.clearWishList()
            Toast.makeText(context, "Favorite List Cleared!", Toast.LENGTH_SHORT).show()
            moviesList=ArrayList()
            adapter.setMovieList(moviesList)
        }
    }

    private fun observeData() {

        viewModel.getFavoriteMoviesList().observe(viewLifecycleOwner,
            { favoriteMovies ->
                if (favoriteMovies.isEmpty() || favoriteMovies == null) {
                    binding.placeHolderText.visibility = View.VISIBLE
                    binding.noItemsPlaceHolder.visibility = View.VISIBLE
                } else {
                    binding.placeHolderText.visibility = View.GONE
                    binding.noItemsPlaceHolder.visibility = View.GONE
                    adapter.setMovieList(favoriteMovies)
                    moviesList = favoriteMovies
                }
            })
    }
    private fun intiRecyclerView() {
        binding.favListRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        adapter = FavoriteAdapter(requireContext(), moviesList)
        binding.favListRecyclerView.adapter = adapter
    }

}