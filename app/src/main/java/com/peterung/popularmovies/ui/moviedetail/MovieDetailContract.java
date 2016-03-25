package com.peterung.popularmovies.ui.moviedetail;

import com.squareup.sqlbrite.SqlBrite;

public interface MovieDetailContract {

    interface View {
        void showMovieInfo(SqlBrite.Query query);

        void showReviews(SqlBrite.Query query);

        void showTrailers(SqlBrite.Query query);

        void showNoMovieSelected();

        void showTrailer(String youtubeUrl);

        void showFavorite(boolean favorite);

    }

    interface UserActionListener {
        void loadMovieInfo(long movieId);
        void loadReviews(long movieId);
        void loadTrailers(long movieId);
        void setView(View view);
        void playTrailer(String trailerKey);
        void setFavorite(long movieId, boolean favorite);

        void unsubscribe();
    }
}
