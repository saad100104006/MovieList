package com.tanvir.xxnetwork.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tanvir.xxnetwork.Utils.Constants
import com.tanvir.xxnetwork.adapter.CategoryMoviesAdapter
import com.tanvir.xxnetwork.databinding.MoviesLayoutBinding
import com.tanvir.xxnetwork.model.Genre
import com.tanvir.xxnetwork.model.Movie
import com.tanvir.xxnetwork.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class MoviesFragment() : Fragment() {
    lateinit var binding: MoviesLayoutBinding
    lateinit var viewModel: MainViewModel
    lateinit var map: HashMap<String, String>
    private var moviesCategory = ""
    private var genreName = ""
    private var genreId = 0
    lateinit var adapter: CategoryMoviesAdapter
    var moviesList: ArrayList<Movie> = ArrayList()
    var currentPage = 0
    var totalPage = 0
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = MoviesLayoutBinding.inflate(inflater, container, false)
        val view = binding.root
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        map = HashMap()
        val args = MoviesFragmentArgs.fromBundle(requireArguments())
        moviesCategory = args.movieCategory
        genreName = args.genreName
        genreId = args.genreId
        map["api_key"] = Constants.API_KEY
        map["page"] = "1"

        initRecyclerView()
        observeData()
        getMoviesList()
    }
    private fun initRecyclerView() {
        binding.moviesRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        adapter = CategoryMoviesAdapter(requireContext(), moviesList, false)
        binding.moviesRecyclerView.adapter = adapter
        binding.moviesRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (currentPage < totalPage) {
                    val layoutManager = recyclerView.layoutManager as GridLayoutManager
                    val total = layoutManager.itemCount
                    val currentLastItem = layoutManager.findLastVisibleItemPosition()
                    if (currentLastItem == total - 1) {
                        currentPage++
                        binding.loading = true
                        viewModel.getMovieByGenre(Genre(genreId, genreName), currentPage)
                    }
                }
            }
        })
    }
    private fun observeData() {
        viewModel.getGenreMovie().observe(viewLifecycleOwner, { genreMovie ->
            if (genreMovie.page == 1) {
                moviesList.clear()
            }
            currentPage = genreMovie.page
            totalPage = genreMovie.total_pages
            moviesList.addAll(genreMovie.results)
            adapter.setMovieList(moviesList)
            binding.loading = false
        })
    }
    private fun getMoviesList() {
        viewModel.getMovieByGenre(Genre(genreId, genreName), 1)
        binding.moviesCategoryTitle.text = genreName
    }
}