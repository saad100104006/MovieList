Modules:
# Home

A. Search:


When a user taps on search bar, search history list should be shown (if available) with the ability to clear search history.
Search results should be shown after user taps “Search” whether its a button or a keyboard done key.
Clicking on a movie should take the user to “Movie Details” screen.


B. Discover:


This is where you list random movies categorized by genre (i.e. Drama, Comedy, etc.)
Each item -movie- in the list should only show name and poster.
Max value is 7 movies per genre.
There should be “See All” button in each genre section.
When a user taps “See All” button, show a new screen with all movies (name and poster) for this genre in an infinite scrolling.
Tapping on a movie should take the user to “Movie Details” screen.

# Movie Details

A. Movie details:


User can see the movie name, short description, rating and poster.
User can add/remove the movie to/from watch list which will be accessible in “Watch List” screen.
User should see an indicator showing if this movie is already added to the watch list.

# Watch List

List of all saved movies (name and poster) in an invite scrolling.
Tapping on a movie should take the user to “Movie Details” screen.



 Used offline storage (Room) for the watch list and search history.

 Added unit tests.

 Used MVVM and Rx.

 Followed Dependency Injection best practices.

 Implemented neat and elegant UI with good UX that fit nicely on different screens sizes

 Handled loading, error and empty states.

 Support Android versions starting from 27


BONUS

Use offline storage to cache the results in “Home” screen for offline loading.
Use CI flow.



# How To Install
1. Clone the master branch
2. Open the project in Android Studio
3. Build the gradle and run
4. Here is an APK for quick check - https://www.dropbox.com/s/afdxfxi7smfiwmd/movie_discovery.apk?dl=0
5. The TMDB API key file is in Utils/Constants file, you can replace it with your API key if you want by changing the value of  const val API_KEY = "3e5241861cd11e025d580d1f50cfec2e"


# How To Use
1. In the home screen there is a serach bar from where we can serach for nay movies by name, there is a serach history feature also available
2. In Homescreen all movies are listed by Genre, which comes randomly. Taping on a See All will take us to a list of the same Genre movie list with an infinite scrolling.
3. Clicking on a movie will open movie details screen
4. In Movie Detail screen we can add a movie into favurire, we can also see similar movie list. Tapping on any similar moview will open that similar movie. See All function will show all similar movies in an infinite scroll view
