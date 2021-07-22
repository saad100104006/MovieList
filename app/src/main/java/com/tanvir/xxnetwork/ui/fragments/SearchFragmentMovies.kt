package com.tanvir.xxnetwork.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.tanvir.xxnetwork.Utils.Constants
import com.tanvir.xxnetwork.adapter.SearchAdapter
import com.tanvir.xxnetwork.databinding.SearchMoviesLayoutBinding
import com.tanvir.xxnetwork.model.Movie
import com.tanvir.xxnetwork.model.SearchSuggestion
import com.tanvir.xxnetwork.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class SearchFragmentMovies : Fragment() {
    lateinit var binding: SearchMoviesLayoutBinding
    lateinit var viewModel: MainViewModel
    lateinit var queryMap: HashMap<String, String>
    lateinit var adapter: SearchAdapter
    private val moviesList: ArrayList<Movie> = ArrayList()
    private val searchSuggestions: ArrayList<SearchSuggestion> = ArrayList()
    private var queryText = ""
    lateinit var popupMenu: PopupMenu;
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SearchMoviesLayoutBinding.inflate(inflater, container, false)
        val view = binding.root
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = SearchFragmentMoviesArgs.fromBundle(requireArguments())
        queryMap = HashMap()
        queryMap["api_key"] = Constants.API_KEY
        queryMap["query"] = args.queryText
        binding.searchKeyword.setText(args.queryText)
        initRecyclerView()
        observeData()
        viewModel.getQueriedMovies(queryMap)
        binding.searchMovie.setOnClickListener {
            queryText = binding.searchKeyword.text.toString().trim().toLowerCase()
            queryMap.clear()
            queryMap["api_key"] = Constants.API_KEY
            queryMap["query"] = queryText
            viewModel.getQueriedMovies(queryMap)
            if (queryText.isNotEmpty()) {
                val searchSuggestion = SearchSuggestion()
                searchSuggestion.term = queryText
                viewModel.insertSearchSuggestion(searchSuggestion)
            }
        }
        binding.searchKeyword.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                queryText = binding.searchKeyword.text.toString().trim().toLowerCase()
                queryMap.clear()
                queryMap["api_key"] = Constants.API_KEY
                queryMap["query"] = queryText
                viewModel.getQueriedMovies(queryMap)
                if (queryText.isNotEmpty()) {
                    val searchSuggestion = SearchSuggestion()
                    searchSuggestion.term = queryText
                    viewModel.insertSearchSuggestion(searchSuggestion)
                }

            }
            false
        }
        binding.searchKeyword.setOnFocusChangeListener(object : View.OnFocusChangeListener {
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if (hasFocus) {
                    popupMenu.show()
                }
            }

        })
    }
    private fun initRecyclerView() {
        binding.searchMoviesRecyclerView.layoutManager =
            StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        adapter = SearchAdapter(requireContext(), moviesList)
        binding.searchMoviesRecyclerView.adapter = adapter

    }
    private fun observeData() {
        viewModel.getQueriesMovies().observe(viewLifecycleOwner,
                { movies ->

                    adapter.setMoviesList(
                        movies
                    )
                })
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
                popupMenu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
                    override fun onMenuItemClick(item: MenuItem?): Boolean {
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
                        return true
                    }
                })
            }
        })
    }

    fun onSearchSuggestionClick(term: String) {
        binding.searchKeyword.setText(term)
        queryMap.clear()
        queryMap["api_key"] = Constants.API_KEY
        queryMap["query"] = term
        viewModel.getQueriedMovies(queryMap)
    }

}