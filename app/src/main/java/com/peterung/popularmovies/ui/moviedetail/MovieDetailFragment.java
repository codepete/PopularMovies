package com.peterung.popularmovies.ui.moviedetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.peterung.popularmovies.NetworkConstants;
import com.peterung.popularmovies.PopularMoviesApplication;
import com.peterung.popularmovies.R;
import com.peterung.popularmovies.data.api.Movie;
import com.peterung.popularmovies.data.api.MovieService;
import com.peterung.popularmovies.ui.movies.MainActivity;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MovieDetailFragment extends Fragment implements MovieDetailContract.View {

    @Inject MovieService mMovieService;

    @Bind(R.id.movie_description_header) TextView mDescriptionHeader;

    @Bind(R.id.movie_description) TextView mDescription;

    @Bind(R.id.movie_release_date) TextView mReleaseDate;

    @Bind(R.id.backdrop) ImageView mBackdrop;

    @Nullable @Bind(R.id.poster) ImageView mPoster;

    @Bind(R.id.movie_rating) TextView mRating;

    @Bind(R.id.vote_count) TextView mVoteCount;

    @Nullable @Bind(R.id.title) TextView mTitle;

    @Nullable @Bind(R.id.toolbar) Toolbar mToolbar;

    @Nullable @Bind(R.id.collapsing_toolbar) CollapsingToolbarLayout mCollapsibleToolbar;


    MovieDetailPresenter mMovieDetailPresenter;
    int mMovieId;
    boolean mTwoPane;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((PopularMoviesApplication) getActivity().getApplication()).getComponent().inject(this);
        mMovieDetailPresenter = new MovieDetailPresenter(this, mMovieService);

        if (savedInstanceState != null) {
            mMovieId = savedInstanceState.getInt("movieId");
            return;
        }

        Bundle args = getArguments();
        if (args != null) {
            mMovieId = args.getInt("movieId");
        } else {
            mMovieId = -1;
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        mMovieDetailPresenter.loadMovieInfo(mMovieId);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, view);

        if (mCollapsibleToolbar != null) {
            MovieDetailActivity movieDetailActivity = (MovieDetailActivity) getActivity();
            movieDetailActivity.setSupportActionBar(mToolbar);
            if (movieDetailActivity.getSupportActionBar() != null) {
                movieDetailActivity.getSupportActionBar().setDisplayShowHomeEnabled(true);
                movieDetailActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }

            mTwoPane = false;
        } else {
            mTwoPane = true;
            if (mToolbar != null) {
                MainActivity mainActivity = (MainActivity) getActivity();
                mToolbar.setTitle("");
                mainActivity.setSupportActionBar(mToolbar);
            }
        }

        return view;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("movieId", mMovieId);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mMovieDetailPresenter.unsubscribe();
    }

    @Override
    public void showMovieInfo(Movie movie) {
        mDescription.setText(movie.overview);
        mReleaseDate.setText(String.format(Locale.US, "Release Date: %s", movie.releaseDate));
        mVoteCount.setText(String.format(Locale.US, "Vote Count: %d", movie.voteCount));
        mRating.setText(String.format(Locale.US, "Vote Average: %.1f/10", movie.voteAverage));

        if (mCollapsibleToolbar != null) {
            mCollapsibleToolbar.setTitle(movie.title);
        }

        Picasso.with(getContext())
                .load(NetworkConstants.MOVIE_DB_IMAGE_URL + "w780" + movie.backdropPath)
                .into(mBackdrop);

        // Tablet view
        if (mPoster != null) {
            mTitle.setText(movie.title);
            Picasso.with(getContext())
                    .load(NetworkConstants.MOVIE_DB_IMAGE_URL + "w342" + movie.posterPath)
                    .into(mPoster);
        }
    }

    @Override
    public void showNoMovieSelected() {
        mDescriptionHeader.setVisibility(View.INVISIBLE);
    }

}
