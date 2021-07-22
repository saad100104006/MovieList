package com.tanvir.xxnetwork.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "search_suggestion")
data class SearchSuggestion(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var term: String = ""
)
