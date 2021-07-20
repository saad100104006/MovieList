package com.tanvir.xxnetwork.model


data class MovieGenre(
    val page: Int,
    var genreName: String,
    var genreId: Int,
    val results: ArrayList<Movie>,
    val total_pages: Int
)
