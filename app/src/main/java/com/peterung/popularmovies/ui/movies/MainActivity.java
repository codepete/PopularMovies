package com.peterung.popularmovies.ui.movies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.peterung.popularmovies.PopularMoviesApplication;
import com.peterung.popularmovies.R;
import com.peterung.popularmovies.ui.moviedetail.MovieDetailActivity;
import com.peterung.popularmovies.ui.moviedetail.MovieDetailFragment;
import com.peterung.popularmovies.utility.MovieDbUtility;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity implements MoviesFragment.MovieItemListener {
    boolean mTwoPane;

    @Inject
    MovieDbUtility mMovieDbUtility;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.movie_detail_fragment_container) != null) {
            mTwoPane = true;

            if (savedInstanceState == null) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.movie_detail_fragment_container, new MovieDetailFragment())
                        .commit();
            }
        } else {
            mTwoPane = false;
        }

        ((PopularMoviesApplication) getApplication()).getComponent().inject(this);
        mMovieDbUtility.getSyncAccount();
    }

    @Override
    public void onMovieClick(long movieId) {

        if (mTwoPane) {
            Bundle bundle = new Bundle();
            bundle.putLong("movieId", movieId);

            MovieDetailFragment fragment = new MovieDetailFragment();
            fragment.setArguments(bundle);

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.movie_detail_fragment_container, fragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, MovieDetailActivity.class);
            intent.putExtra("movieId", movieId);
            startActivity(intent);
        }
    }
}
