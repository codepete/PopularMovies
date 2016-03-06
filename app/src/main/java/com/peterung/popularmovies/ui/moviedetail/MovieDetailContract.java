package com.peterung.popularmovies.ui.moviedetail;

import com.peterung.popularmovies.data.api.Movie;

public interface MovieDetailContract {

    interface View {
        void showMovieInfo(Movie movie);

        void showNoMovieSelected();

    }

    interface UserActionListener {
        void loadMovieInfo(int movieId);

        void unsubscribe();
    }
}
