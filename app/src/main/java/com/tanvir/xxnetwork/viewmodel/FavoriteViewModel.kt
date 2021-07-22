package com.tanvir.xxnetwork.viewmodel


import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.tanvir.xxnetwork.db.FavoriteMovie
import com.tanvir.xxnetwork.repository.Repository
import javax.inject.Inject

class FavoriteViewModel : ViewModel {
    lateinit var repository: Repository
    lateinit var favoriteMovies: LiveData<List<FavoriteMovie>>
    constructor()
    @ViewModelInject
    constructor(repository: Repository) :super() {
        this.repository=repository
        favoriteMovies=repository.getFavoriteList()
    }

    fun getFavoriteMoviesList(): LiveData<List<FavoriteMovie>> {
        return favoriteMovies
    }

    fun clearWishList() {
        repository.clearFavoriteList()
    }

}
