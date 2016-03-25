package com.peterung.popularmovies.ui.movies;

import com.squareup.sqlbrite.SqlBrite;

import java.io.IOException;

public interface MoviesContract {
    interface View {

        void showMoviesList(SqlBrite.Query query);

        void showMovieDetails(long movieId);

    }

    interface UserActionsListener {

        void setView(MoviesContract.View view);

        void loadMovies(String filter);

        void openMovieDetails(long movieId) throws IOException;

        void unsubscribe();
    }
}
