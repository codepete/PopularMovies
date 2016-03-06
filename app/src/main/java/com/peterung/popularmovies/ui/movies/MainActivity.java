package com.peterung.popularmovies.ui.movies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.peterung.popularmovies.R;
import com.peterung.popularmovies.data.api.Movie;
import com.peterung.popularmovies.ui.moviedetail.MovieDetailActivity;
import com.peterung.popularmovies.ui.moviedetail.MovieDetailFragment;

public class MainActivity extends AppCompatActivity implements MoviesFragment.MovieItemListener {
    boolean mTwoPane;

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
    }

    @Override
    public void onMovieClick(Movie movie) {

        if (mTwoPane) {
            Bundle bundle = new Bundle();
            bundle.putInt("movieId", movie.id);

            MovieDetailFragment fragment = new MovieDetailFragment();
            fragment.setArguments(bundle);

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.movie_detail_fragment_container, fragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, MovieDetailActivity.class);
            intent.putExtra("movieId", movie.id);
            startActivity(intent);
        }
    }
}
