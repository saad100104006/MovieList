package com.tanvir.xxnetwork.viewModelTest

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.tanvir.xxnetwork.Utils.Constants
import com.tanvir.xxnetwork.db.FavoriteDao
import com.tanvir.xxnetwork.model.Genre
import com.tanvir.xxnetwork.model.Movie
import com.tanvir.xxnetwork.model.MovieGenre
import com.tanvir.xxnetwork.model.MovieResponse
import com.tanvir.xxnetwork.network.MovieApiService
import com.tanvir.xxnetwork.repository.Repository
import com.tanvir.xxnetwork.viewmodel.MainViewModel
import io.reactivex.rxjava3.core.Observable
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class MovieDetailViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    @get:Rule
    var rxJavaRule: TestRule = TestRxJavaRule()
    @Mock
    private lateinit var movieServiceApi: MovieApiService
    @Mock
    private lateinit var favoriteDao: FavoriteDao
    @Mock
    lateinit var lifecycleOwner: LifecycleOwner
    private lateinit var repository: Repository
    private lateinit var movie: Movie
    private lateinit var movieGenre: MovieGenre
    private lateinit var movieResponse: MovieResponse
    var lifecycle: Lifecycle? = null
    lateinit var viewModel: MainViewModel
    var id = 1
    var vote_count = 50
    var popularity = 7


    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        lifecycle = LifecycleRegistry(lifecycleOwner)
        movieGenre = MovieGenre(1, "Action", 28, arrayListOf(), 10)
        movie = Movie(
            "/sample",
            "sample overview",
            "01-07-2020",
            "Luca",
            "/sample2",
            id,
            vote_count,
            id,
            popularity,
            1,
            arrayListOf(),
            arrayListOf(),
            arrayListOf()
        )
        movieResponse = MovieResponse(1, 1, 1, arrayListOf(movie))

        repository = Repository(movieApiService = movieServiceApi, favoriteDao)
        viewModel = MainViewModel(repository)
    }

    @Test
    fun test_movie_details() {
        val queryMap: HashMap<String, String> = HashMap()
        val movieId = 28
        queryMap["api_key"] = Constants.API_KEY
        queryMap["page"] = "1"
        `when`(movieServiceApi.getMovieDetails(movieId, queryMap)).thenReturn(Observable.just(movie))
        viewModel.getMovieDetails(movieId, queryMap)
        viewModel.getMovie().value.let {
            assertTrue(it?.id == id)
            assertTrue(it?.title == "Luca")
            assertTrue(it?.overview == "sample overview")
            assertTrue(it?.poster_path == "/sample")
        }

    }

    @Test
    fun test_movies() {
        val genre = Genre(28, "Action")
        `when`(
            movieServiceApi.getMovieByGenre(
                Constants.API_KEY,
                28,
                1
            )
        ).thenReturn(Observable.just(movieGenre))
        viewModel.getMovieByGenre(genre, 1)
        viewModel.getGenreMovie().value.let {
            assertTrue(it?.genreName == "Action")
            assertTrue(it?.genreId == 28)
        }
    }

    @Test
    fun test_similar_movies() {
        val queryMap: HashMap<String, String> = HashMap()
        queryMap["api_key"] = Constants.API_KEY
        queryMap["page"] = "1"
        `when`(movieServiceApi.getSimilarMovies(1, 1, queryMap)).thenReturn(Observable.just(movieResponse))
        viewModel.getSimilarMovies(1, 1, queryMap)
        viewModel.getMovieListSimilar().value.let {
            assertTrue(it?.get(0)?.id == id)
            assertTrue(it?.get(0)?.title == "Luca")
        }
    }

}