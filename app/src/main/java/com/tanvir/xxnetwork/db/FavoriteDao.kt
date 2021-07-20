package com.tanvir.xxnetwork.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.tanvir.xxnetwork.model.SearchSuggestion

@Dao
interface FavoriteDao {
    @Insert
    fun insert(favoriteMovie: FavoriteMovie)

    @Query("DELETE From favorite_table WHERE id = :movieId")
    fun delete(movieId: Int)

    @Query("DELETE FROM favorite_table")
    fun clearFavoriteList()

    @Query("SELECT * FROM favorite_table")
    fun getFavoriteList(): LiveData<List<FavoriteMovie>>

    @Query("SELECT * FROM favorite_table WHERE id = :movieId ")
    fun getFavoriteListMovie(movieId: Int): FavoriteMovie

    @Insert
    fun insertSearchSuggestion(searchSuggestion: SearchSuggestion)

    @Query("SELECT * FROM search_suggestion")
    fun getSearchSuggestions(): LiveData<List<SearchSuggestion>>

    @Query("DELETE FROM search_suggestion")
    fun clearSearchSuggestion()
}