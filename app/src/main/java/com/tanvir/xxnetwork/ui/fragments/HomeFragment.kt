package com.tanvir.xxnetwork.ui.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import kotlin.random.Random
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.PopupMenu
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.tanvir.xxnetwork.Utils.Constants
import com.tanvir.xxnetwork.adapter.GenreMovieAdapter
import com.tanvir.xxnetwork.databinding.HomeLayoutBinding
import com.tanvir.xxnetwork.model.MovieGenre
import com.tanvir.xxnetwork.model.SearchSuggestion
import com.tanvir.xxnetwork.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private val TAG = "Home"
    lateinit var viewModel: MainViewModel
    lateinit var binding: HomeLayoutBinding
    private val map = HashMap<String, String>()
    private var queryText = ""
    lateinit var queryMap: HashMap<String, String>
    private val searchSuggestions: ArrayList<SearchSuggestion> = ArrayList()
    lateinit var popupMenu: PopupMenu
    lateinit var genreMovieAdapter: GenreMovieAdapter

    var genres = Constants.getGenres()

    var currentGenre: Int = 0
    var genreMovies: ArrayList<MovieGenre> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = HomeLayoutBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this@HomeFragment).get(MainViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        map["api_key"] = Constants.API_KEY
        map["page"] = "1"
        binding.progressBar.visibility = View.VISIBLE
        observeData()
        getMoviesList()
        setUpRecyclerViewsAndViewPager()
        shuffle(genres)
        binding.searchMovie.setOnClickListener {
            queryMap = HashMap()
            queryText = binding.searchKeyword.text.toString().trim().toLowerCase()
            queryMap.clear()
            queryMap["api_key"] = Constants.API_KEY
            queryMap["query"] = queryText

            if (queryText.isNotEmpty()) {
                val searchSuggestion = SearchSuggestion()
                searchSuggestion.term = queryText
                viewModel.insertSearchSuggestion(searchSuggestion)
            }

            val action: HomeFragmentDirections.ActionHome2ToSearch =
                HomeFragmentDirections.actionHome2ToSearch()
            action.queryText = queryText
            Navigation.findNavController(it).navigate(action)
        }

        binding.searchKeyword.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                queryText = binding.searchKeyword.text.toString().trim().toLowerCase()
                queryMap = HashMap()
                queryMap.clear()
                queryMap["api_key"] = Constants.API_KEY
                queryMap["query"] = queryText
                viewModel.getQueriedMovies(queryMap)
                if (queryText.isNotEmpty()) {
                    val searchSuggestion = SearchSuggestion()
                    searchSuggestion.term = queryText
                    viewModel.insertSearchSuggestion(searchSuggestion)
                }

                val action: HomeFragmentDirections.ActionHome2ToSearch =
                    HomeFragmentDirections.actionHome2ToSearch()
                action.queryText = queryText
                Navigation.findNavController(textView).navigate(action)

            }

            false
        }

        binding.searchKeyword.onFocusChangeListener =
            View.OnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    popupMenu.show()
                }
            }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onStart() {
        super.onStart()
        observeData()
        binding.progressBar.visibility = View.VISIBLE
        if (Constants.isNetworkAvailable(requireActivity())) {
            getMoviesList()
        } else {
            observeData()
            Toast.makeText(activity, "No Internet Connection", Toast.LENGTH_SHORT).show()
        }
    }

    private fun observeData() {
        viewModel.getSearchSuggestions().observe(viewLifecycleOwner, { suggestions ->
            searchSuggestions.clear()
            searchSuggestions.addAll(suggestions)
            searchSuggestions.reverse()
            popupMenu = PopupMenu(requireContext(), binding.searchKeyword)

            for (s in searchSuggestions) {
                popupMenu.menu.add(s.term)
            }
            if (searchSuggestions.isNotEmpty()) {
                popupMenu.menu.add("Clear Search History")
                popupMenu.setOnMenuItemClickListener { item ->
                    if (item.toString() == "Clear Search History") {
                        MaterialAlertDialogBuilder(requireContext())
                            .setTitle("Are You Sure?")
                            .setPositiveButton("Yes") { dialog, which -> viewModel.clearSearchSuggestion() }
                            .setNegativeButton("No") { dialog, which -> dialog.dismiss() }
                            .show()
                    } else {
                        onSearchSuggestionClick(item.toString())
                    }
                    binding.searchKeyword.clearFocus()
                    true
                }
            }
        })

        viewModel.getGenreMovie().observe(viewLifecycleOwner, { movieGenre ->
            if (movieGenre == null) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
            if (!contain(movieGenre)) {
                genreMovies.add(movieGenre)
            }
            genreMovieAdapter.notifyDataSetChanged()
            currentGenre++
            if (currentGenre < genres.size) {
                viewModel.getMovieByGenre(genres[currentGenre], 1)

            }
        })

    }

    private fun getMoviesList() {
        try {
            viewModel.getMovieByGenre(genres[currentGenre], 1)

        } catch (e: Exception) {
        }
    }

    private fun contain(genre: MovieGenre): Boolean {
        for (g in genreMovies) {
            if (g.genreName == genre.genreName) {
                return true
            }
        }
        return false
    }

    private fun setUpRecyclerViewsAndViewPager() {
        genreMovieAdapter = GenreMovieAdapter(requireContext(), genreMovies)
        binding.genreMoviesRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.genreMoviesRecycler.adapter = genreMovieAdapter

    }

    fun onSearchSuggestionClick(term: String) {
        binding.searchKeyword.setText(term)
        queryMap.clear()
        queryMap["api_key"] = Constants.API_KEY
        queryMap["query"] = term
        viewModel.getQueriedMovies(queryMap)
    }

    fun <T> shuffle(list: MutableList<T>) {
        //create random genre each time
        for (i in list.size - 1 downTo 1) {
            val j = Random.nextInt(i + 1)
            val temp = list[i]
            list[i] = list[j]
            list[j] = temp
        }
    }
}