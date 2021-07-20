package com.tanvir.xxnetwork.model

import com.google.gson.JsonObject

data class Movie(
    val poster_path:String,
    val overview:String,
    val release_date:String,
    val title:String,
    val backdrop_path:String,
    val id:Int,
    val vote_count:Int,
    val runtime:Int,
    val popularity:Number,
    val vote_average:Number,
    val genre_ids:ArrayList<Int>,
    var genre_names:ArrayList<String>,
    val genres:ArrayList<Genre>,
)
