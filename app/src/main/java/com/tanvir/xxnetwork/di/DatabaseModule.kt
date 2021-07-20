package com.tanvir.xxnetwork.di

import android.app.Application
import androidx.room.Room
import com.tanvir.xxnetwork.Utils.Constants
import com.tanvir.xxnetwork.db.FavoriteDao
import com.tanvir.xxnetwork.db.MovieDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class DatabaseModule {
    @Provides
    @Singleton
    fun provideMovieDatabase(application: Application): MovieDatabase {
        return Room.databaseBuilder(application, MovieDatabase::class.java, Constants.DataBaseName)
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }

    @Provides
    @Singleton
    fun provideFavoriteDao(movieDatabase: MovieDatabase): FavoriteDao {
        return movieDatabase.favoriteDao()
    }

}