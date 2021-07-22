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
        const val MAX_ITEM = 7


        @RequiresApi(Build.VERSION_CODES.M)
        fun isNetworkAvailable(context: Context): Boolean {
            val mConnectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            // Test for connection
            val mCapabilities =
                mConnectivityManager.getNetworkCapabilities(mConnectivityManager.activeNetwork)

            return mCapabilities != null &&
                    (mCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                            mCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
        }
        fun getGenreMap(): HashMap<Int, String> {
            val genreMap = HashMap<Int, String>()
            genreMap[28] = "Action"
            genreMap[12] = "Adventure"
            genreMap[16] = "Animation"
            genreMap[35] = "Comedy"
            genreMap[80] = "Crime"
            genreMap[99] = "Documentary"
            genreMap[18] = "Drama"
            genreMap[10751] = "Family"
            genreMap[14] = "Fantasy"
            genreMap[36] = "History"
            genreMap[27] = "Horror"
            genreMap[10402] = "Music"
            genreMap[9648] = "Mystery"
            genreMap[10749] = "Romance"
            genreMap[878] = "Science Fiction"
            genreMap[53] = "Thriller"
            genreMap[10752] = "War"
            genreMap[37] = "Western"
            genreMap[10770] = "TV Movie"
            return genreMap
        }

        fun getGenres(): ArrayList<Genre> {
            val genreName = ArrayList<Genre>()
            genreName.add(Genre(28, "Action"))
            genreName.add(Genre(12, "Adventure"))
            genreName.add(Genre(16, "Animation"))
            genreName.add(Genre(35, "Comedy"))
            genreName.add(Genre(80, "Crime"))
            genreName.add(Genre(99, "Documentary"))
            genreName.add(Genre(18, "Drama"))
            genreName.add(Genre(10751, "Family"))
            genreName.add(Genre(14, "Fantasy"))
            genreName.add(Genre(36, "History"))
            genreName.add(Genre(27, "Horror"))
            genreName.add(Genre(10402, "Music"))
            genreName.add(Genre(9648, "Mystery"))
            genreName.add(Genre(10749, "Romance"))
            genreName.add(Genre(878, "Science Fiction"))
            genreName.add(Genre(53, "Thriller"))
            genreName.add(Genre(10752, "War"))
            genreName.add(Genre(37, "Western"))
            genreName.add(Genre(10770, "TV Movie"))
            return genreName
        }
    }

}
