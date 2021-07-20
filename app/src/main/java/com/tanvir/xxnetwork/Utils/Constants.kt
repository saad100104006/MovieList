package com.tanvir.xxnetwork.Utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import com.tanvir.xxnetwork.model.Genre
import java.util.*

class Constants {
    companion object {
        const val API_KEY = "3e5241861cd11e025d580d1f50cfec2e"
        const val BaseURL = "https://api.themoviedb.org/3/"
        const val ImageBaseURL = "https://image.tmdb.org/t/p/original"
        const val ImageBaseURLw500 = "https://image.tmdb.org/t/p/w500"
        const val DataBaseName = "FavoriteDB"
        const val GENRE = "Genre"
    }

}
