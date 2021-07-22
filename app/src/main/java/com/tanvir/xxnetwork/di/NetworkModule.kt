package com.tanvir.xxnetwork.di

import com.tanvir.xxnetwork.BaseApplication
import com.tanvir.xxnetwork.Utils.Constants
import com.tanvir.xxnetwork.network.MovieApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
class NetworkModule {
    @Provides
    @Singleton
    fun provideMovieApiService(): MovieApiService {

        //add cache implementation
        val onlineInterceptor: Interceptor = Interceptor { chain ->
            val response: Response = chain.proceed(chain.request())
            val maxAge = 60 // read from cache for 60 seconds even if there is internet connection
            response.newBuilder()
                    .header("Cache-Control", "public, max-age=$maxAge")
                    .removeHeader("Pragma")
                    .build()
        }

        val offlineInterceptor = Interceptor { chain ->
            var request: Request = chain.request()
            if (!Constants.isNetworkAvailable(BaseApplication.applicationContext())) {
                val maxStale = 60 * 60 * 24 * 30 // Offline cache available for 30 days
                request = request.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
                        .removeHeader("Pragma")
                        .build()
            }
            chain.proceed(request)
        }
        val cacheSize = 10 * 1024 * 1024 // 10 MB

        val cache = Cache(BaseApplication.applicationContext().cacheDir, cacheSize.toLong())

        val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(offlineInterceptor)
                .addNetworkInterceptor(onlineInterceptor)
                .cache(cache)
                .build()

        return Retrofit.Builder()
                .baseUrl(Constants.BaseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .client(okHttpClient)
                .build()
                .create(MovieApiService::class.java)
    }
}