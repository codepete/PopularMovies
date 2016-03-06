package com.peterung.popularmovies.ui.movies;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.peterung.popularmovies.PopularMoviesApplication;
import com.peterung.popularmovies.R;
import com.peterung.popularmovies.data.api.Movie;
import com.peterung.popularmovies.data.api.MovieService;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MoviesFragment extends Fragment implements MoviesContract.View, AdapterView.OnItemSelectedListener {
    String LOG_TAG = getClass().getSimpleName();

    @Inject MovieService mMovieService;

    @Bind(R.id.movies_list) RecyclerView mRecyclerView;

    @Bind(R.id.toolbar) Toolbar mToolbar;

    @Bind(R.id.spinner_nav) Spinner mSpinner;

    MoviesAdapter mMoviesAdapter;
    GridLayoutManager mGridLayoutManager;
    MovieItemListener mMovieItemListener;
    MoviesPresenter mMoviesPresenter;
    int mPosition;

    public MoviesFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((PopularMoviesApplication) getActivity().getApplication()).getComponent().inject(this);
        mMoviesPresenter = new MoviesPresenter(mMovieService, this);
        mMoviesAdapter = new MoviesAdapter(getContext(), new ArrayList<Movie>(0), mMoviesPresenter);
        mPosition = RecyclerView.NO_POSITION;

        if (savedInstanceState != null) {
            mPosition = savedInstanceState.getInt("pos");
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        mMoviesPresenter.loadMovies(null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        ButterKnife.bind(this, view);
        mGridLayoutManager = new GridLayoutManager(getContext(), 2);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setAdapter(mMoviesAdapter);

        mToolbar.setTitle("");
        ((MainActivity) getActivity()).setSupportActionBar(mToolbar);
        SpinnerAdapter spinnerAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.movie_filter_title, R.layout.spinner_item);
        mSpinner.setAdapter(spinnerAdapter);
        mSpinner.setOnItemSelectedListener(this);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mMovieItemListener = (MovieItemListener) context;
        } catch (ClassCastException ex) {
            Log.e(LOG_TAG, ex.getMessage());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mPosition != RecyclerView.NO_POSITION) {
            outState.putInt("pos", mGridLayoutManager.findFirstCompletelyVisibleItemPosition());
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mMoviesPresenter.unsubscribe();
    }

    @Override
    public void showMoviesList(List<Movie> movies) {
        mMoviesAdapter.replaceData(movies);
        if (mPosition != RecyclerView.NO_POSITION) {
            mRecyclerView.scrollToPosition(mPosition);
        }
    }

    @Override
    public void showMovieDetails(int pos, Movie movie) {
        mPosition = pos;
        mMovieItemListener.onMovieClick(movie);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String[] filterValues = getResources().getStringArray(R.array.movie_filter_values);
        String filter = filterValues[position];

        mMoviesPresenter.loadMovies(filter);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Don't do anything
    }


    public interface MovieItemListener {
        void onMovieClick(Movie movie);
    }
}
