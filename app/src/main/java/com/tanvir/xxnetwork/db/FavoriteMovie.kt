package com.tanvir.xxnetwork.db

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_table")
data class FavoriteMovie(
    @PrimaryKey(autoGenerate = true)
    @NonNull
    val id: Int,
    val poster_path: String,
    val overview: String,
    val release_date: String,
    val title: String,
    val backdrop_path: String,
    val vote_count: Int,
    val runtime: Int
)