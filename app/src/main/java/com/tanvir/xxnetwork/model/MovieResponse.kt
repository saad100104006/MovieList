package com.tanvir.xxnetwork.model

data class MovieResponse(
    val page:Int,
    val total_pages:Int,
    val total_results:Int,
    val results:ArrayList<Movie>
)
