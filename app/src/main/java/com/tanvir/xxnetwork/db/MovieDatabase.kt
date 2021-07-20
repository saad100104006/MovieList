package com.tanvir.xxnetwork.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tanvir.xxnetwork.model.SearchSuggestion

@Database(
    entities = [FavoriteMovie::class, SearchSuggestion::class],
    version = 2,
    exportSchema = false
)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
}