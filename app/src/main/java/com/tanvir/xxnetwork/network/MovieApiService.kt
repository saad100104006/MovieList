package com.tanvir.xxnetwork.network

import com.google.gson.JsonObject
import com.tanvir.xxnetwork.model.Movie
import com.tanvir.xxnetwork.model.MovieGenre
import com.tanvir.xxnetwork.model.MovieResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap
import java.util.*

interface MovieApiService {

    @GET("movie/{movie_id}")
    fun getMovieDetails(@Path("movie_id") id: Int, @QueryMap queries: HashMap<String, String>): Observable<Movie>

    @GET("movie/{movie_id}/similar")
    fun getCast(@Path("movie_id") id: Int, @Query("page") page:Int, @QueryMap queries: HashMap<String, String>): Observable<MovieResponse>

    @GET("search/movie")
    fun getMoviesBySearch(@QueryMap queries: HashMap<String, String>): Observable<JsonObject>

    @GET("discover/movie")
    fun getMovieByGenre(
        @Query("api_key") api_key: String,
        @Query("with_genres") with_genres: Int, @Query("page") page: Int

    ): Observable<MovieGenre>


}