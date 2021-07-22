package com.tanvir.xxnetwork.repository

import androidx.lifecycle.LiveData
import com.google.gson.JsonObject
import com.tanvir.xxnetwork.Utils.Constants
import com.tanvir.xxnetwork.db.FavoriteDao
import com.tanvir.xxnetwork.db.FavoriteMovie
import com.tanvir.xxnetwork.model.*
import com.tanvir.xxnetwork.network.MovieApiService
import io.reactivex.rxjava3.core.Observable
import java.util.*
import javax.inject.Inject

class Repository  {
    lateinit var movieApiService: MovieApiService
    lateinit var favoriteDao: FavoriteDao

    @Inject
    constructor(movieApiService: MovieApiService, favoriteDao: FavoriteDao) {
        this.movieApiService = movieApiService
        this.favoriteDao = favoriteDao
    }

    fun getMovieDetails(movieId: Int, map: HashMap<String, String>): Observable<Movie> {
        return movieApiService.getMovieDetails(movieId, map)
    }

    fun getSimilarMovies(movieId: Int, page:Int, map: HashMap<String, String>): Observable<MovieResponse>{
        return movieApiService.getSimilarMovies(movieId, page, map)
    }

    fun getMoviesBySearch(map: HashMap<String, String>): Observable<JsonObject> {
        return movieApiService.getMoviesBySearch(map)
    }

    fun getMovieByGenre(genre: Int, page: Int): Observable<MovieGenre> {
        return movieApiService.getMovieByGenre(Constants.API_KEY, genre, page)
    }

    fun insertMovie(favoriteMovie: FavoriteMovie) {
        favoriteDao.insert(favoriteMovie)
    }

    fun deleteMovie(movieId: Int) {
        favoriteDao.delete(movieId)
    }

    fun clearFavoriteList() {
        favoriteDao.clearFavoriteList()
    }

    fun getFavoriteList(): LiveData<List<FavoriteMovie>> {
        return favoriteDao.getFavoriteList()
    }

    fun getFavoriteListMovie(movieId: Int): FavoriteMovie {
        return favoriteDao.getFavoriteListMovie(movieId)
    }

    fun insertSearchSuggestion(searchSuggestion: SearchSuggestion) {
        favoriteDao.insertSearchSuggestion(searchSuggestion)
    }

    fun getSearchSuggestions(): LiveData<List<SearchSuggestion>> {
        return favoriteDao.getSearchSuggestions()
    }

    fun clearSearchSuggestion() {
        favoriteDao.clearSearchSuggestion()
    }


}