package com.tanvir.xxnetwork.ui.fragments

import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import android.provider.MediaStore.Video
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tanvir.xxnetwork.R
import com.tanvir.xxnetwork.Utils.Constants
import com.tanvir.xxnetwork.adapter.SimilarMovieAdapter
import com.tanvir.xxnetwork.databinding.MovieDetailsLayoutBinding
import com.tanvir.xxnetwork.db.FavoriteMovie
import com.tanvir.xxnetwork.model.Movie
import com.tanvir.xxnetwork.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class MovieDetailsFragment() : Fragment() {

    private val TAG = "MovieDetails"
    lateinit var binding: MovieDetailsLayoutBinding
    lateinit var viewModel: MainViewModel
    var movieId: Int=0
    lateinit var queryMap: HashMap<String, String>
    lateinit var temp: String
    lateinit var videoId:String
    lateinit var adapter: SimilarMovieAdapter
    var similarList: ArrayList<Movie> = ArrayList()
    private var hour = 0
    private var min:Int = 0
    lateinit var mMovie: Movie
    private var inFavList = false
    private val videos: ArrayList<Video> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = MovieDetailsLayoutBinding.inflate(inflater, container, false)
        val view = binding.root
        viewModel = ViewModelProvider(this@MovieDetailsFragment).get(MainViewModel::class.java)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        similarList = ArrayList()
        queryMap = HashMap()

        val args = MovieDetailsFragmentArgs.fromBundle(requireArguments())
        movieId = args.movieId

        observeData()
        queryMap["api_key"] = Constants.API_KEY
        queryMap["page"] = "1"
        //queryMap["append_to_response"] = "videos"

        viewModel.getMovieDetails(movieId, queryMap)
        viewModel.getSimilarMovies(movieId, 1,queryMap)


        binding.viewAllSimilar.setOnClickListener { v ->
            val action: MovieDetailsFragmentDirections.ActionMovieDetailsToSimilarMovies = MovieDetailsFragmentDirections.actionMovieDetailsToSimilarMovies(movieId)
            action.movieId = movieId
            Navigation.findNavController(v).navigate(action)
        }

        binding.addToFavoriteList.setOnClickListener {
            if (inFavList) {
                viewModel.deleteMovie(movieId)
                binding.addToFavoriteList.setImageResource(R.drawable.ic_playlist)
                Toast.makeText(context, "Removed from Favorite List.", Toast.LENGTH_SHORT).show()
            } else {
                val movie = FavoriteMovie(
                    mMovie.id,
                    mMovie.poster_path,
                    mMovie.overview,
                    mMovie.release_date,
                    mMovie.title,
                    mMovie.backdrop_path,
                    mMovie.vote_count,
                    mMovie.runtime
                )
                try {
                    viewModel.insertMovie(movie)
                } catch (e : SQLiteConstraintException) {
                    e.printStackTrace()
                }

                binding.addToFavoriteList.setImageResource(R.drawable.ic_playlist_add)
                Toast.makeText(context, "Added to Favorite List.", Toast.LENGTH_SHORT).show()
            }
        }

    }
    private fun isMovieInFavList(movieId: Int) {
        inFavList = if (viewModel.getFavoriteListMovie(movieId) != null) {
            binding.addToFavoriteList.setImageResource(R.drawable.ic_playlist_add)
            true
        } else {
            binding.addToFavoriteList.setImageResource(R.drawable.ic_playlist)
            false
        }
        binding.addToFavoriteList.visibility = View.VISIBLE
    }

    private fun observeData() {
        viewModel.getMovie().observe(viewLifecycleOwner,
            { movie ->
                mMovie = movie
                Glide.with(requireContext()).load(Constants.ImageBaseURL + movie.poster_path)
                    .centerCrop()
                    .into(binding.moviePoster)
                binding.movieName.text = movie.title
                hour = movie.runtime / 60
                min = movie.runtime % 60
                binding.movieRuntime.text = hour.toString() + "h " + min + "m"
                binding.moviePlot.text = movie.overview
                binding.movieRatings.text = "Rating: "+ movie.vote_average.toString()
                temp = ""
                for (i in 0 until movie.genres.size) {
                    if (i == movie.genres.size - 1) temp += movie.genres[i].name else temp += movie.genres.get(i).name.toString() + " • "
                }
                binding.movieGenre.text = temp
                binding.movieSimilarText.visibility = View.VISIBLE
                binding.viewAllSimilar.visibility = View.VISIBLE
                binding.moviePlotText.visibility = View.VISIBLE
                isMovieInFavList(movieId)
            })


        viewModel.getMovieListSimilar().observe(viewLifecycleOwner,
            androidx.lifecycle.Observer<java.util.ArrayList<Movie>> { actors ->
                Log.e(TAG, "onChanged: " + actors.size)
                binding.similarMovieRecyclerView.layoutManager =
                        LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                adapter = SimilarMovieAdapter(requireContext(), actors)
                binding.similarMovieRecyclerView.adapter = adapter
                binding.moviePoster.clipToOutline = true
                adapter.notifyDataSetChanged()
            })
    }

}