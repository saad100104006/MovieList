package com.tanvir.xxnetwork.viewmodel

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.tanvir.xxnetwork.db.FavoriteMovie
import com.tanvir.xxnetwork.model.Genre
import com.tanvir.xxnetwork.model.Movie
import com.tanvir.xxnetwork.model.MovieGenre
import com.tanvir.xxnetwork.model.SearchSuggestion
import com.tanvir.xxnetwork.repository.Repository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.functions.Function
import io.reactivex.rxjava3.observers.DisposableObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*

class HomeViewModel  : ViewModel {
    lateinit var repository: Repository
    private val TAG = "HomeViewModel"
    private val queriesMovies: MutableLiveData<ArrayList<Movie>> =
        MutableLiveData<ArrayList<Movie>>()
     val similarMovieList: MutableLiveData<ArrayList<Movie>> = MutableLiveData<ArrayList<Movie>>()
    private val movieDetails: MutableLiveData<Movie> = MutableLiveData<Movie>()
    private val movieGenre: MutableLiveData<MovieGenre> = MutableLiveData<MovieGenre>()
    private val disposable = CompositeDisposable()
    private val similarMoviesTotalPages: MutableLiveData<Int> = MutableLiveData<Int>()


    constructor()
    @ViewModelInject
    constructor(repository: Repository) :super() {
        this.repository=repository
    }


    fun getMovie(): MutableLiveData<Movie> {
        return movieDetails
    }


    fun getSimilarMoviePagesCount(): MutableLiveData<Int> {
        return similarMoviesTotalPages
    }

    fun getMovieCastList(): MutableLiveData<ArrayList<Movie>> {
        return similarMovieList
    }

    fun getQueriesMovies(): MutableLiveData<ArrayList<Movie>> {
        return queriesMovies
    }

    fun getGenreMovie(): MutableLiveData<MovieGenre> {
        return movieGenre
    }

    fun getMovieByGenre(genre: Genre, page: Int) {
        repository.getMovieByGenre(genre.id, page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableObserver<MovieGenre>() {
                override fun onNext(movie: MovieGenre) {
                    movie.genreName = genre.name
                    movie.genreId = genre.id
                    movieGenre.value = movie
                }

                override fun onError(e: Throwable) {

                }

                override fun onComplete() {

                }

            })
    }


    fun getMovieDetails(movieId: Int, map: HashMap<String, String>) {
        disposable.add(repository.getMovieDetails(movieId, map)
            .subscribeOn(Schedulers.io())
            .map { movie ->
                val genreNames = ArrayList<String>()
                for (genre in movie.genres) {
                    genreNames.add(genre.name)
                }
                movie.genre_names=genreNames
                movie
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result -> movieDetails.postValue(result) }
            ) { error -> Log.e(TAG, "getMovieDetails: " + error.message) }
        )
    }


    fun getSimilarMovies(movieId: Int,page:Int, map: HashMap<String, String>) {
        disposable.add(repository.getSimilarMovies(movieId, page,map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    similarMoviesTotalPages.postValue(result.total_pages)
                    similarMovieList.postValue(result.results)

                }
                ) { error -> Log.e(TAG, "getPopularMovies: " + error.message) }
        )
    }
    
    fun getQueriedMovies(map: HashMap<String, String>) {
        disposable.add(repository.getMoviesBySearch(map)
            .subscribeOn(Schedulers.io())
            .map(Function<JsonObject, ArrayList<Movie>?> { jsonObject ->
                val jsonArray = jsonObject.getAsJsonArray("results")
                Gson().fromJson(
                    jsonArray.toString(),
                    object : TypeToken<ArrayList<Movie?>?>() {}.type
                )
            }
            )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result -> queriesMovies.postValue(result) }
            ) { error -> Log.e(TAG, "getPopularMovies: " + error.message) }
        )
    }


    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }


}