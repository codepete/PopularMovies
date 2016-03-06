package com.peterung.popularmovies.ui.movies;

import com.peterung.popularmovies.data.api.Movie;

import java.io.IOException;
import java.util.List;

public interface MoviesContract {
    interface View {

        void showMoviesList(List<Movie> movies);

        void showMovieDetails(int pos, Movie movie);

    }

    interface UserActionsListener {

        void loadMovies(String filter);

        void openMovieDetails(int pos, Movie movie) throws IOException;

        void unsubscribe();
    }
}
