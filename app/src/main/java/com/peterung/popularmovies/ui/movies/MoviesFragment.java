package com.peterung.popularmovies.ui.movies;

import android.content.Context;
import android.database.Cursor;
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

import com.peterung.popularmovies.PopularMoviesApplication;
import com.peterung.popularmovies.R;
import com.peterung.popularmovies.data.api.MovieService;
import com.peterung.popularmovies.data.repository.SharedPreferencesRepository;
import com.squareup.sqlbrite.SqlBrite;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MoviesFragment extends Fragment implements MoviesContract.View, AdapterView.OnItemSelectedListener {
    String LOG_TAG = getClass().getSimpleName();

    @Inject MovieService mMovieService;
    @Inject MoviesPresenter mMoviesPresenter;
    @Inject SharedPreferencesRepository mSharedPreferencesRepository;

    @Bind(R.id.movies_list) RecyclerView mRecyclerView;

    @Bind(R.id.toolbar) Toolbar mToolbar;

    @Bind(R.id.spinner_nav) Spinner mSpinner;

    MoviesAdapter mMoviesAdapter;
    GridLayoutManager mGridLayoutManager;
    MovieItemListener mMovieItemListener;
    Cursor mCursor;
    int mPosition;

    public MoviesFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((PopularMoviesApplication) getActivity().getApplication()).getComponent().inject(this);

        mMoviesAdapter = new MoviesAdapter(getContext(), null, mMoviesPresenter);
        mPosition = RecyclerView.NO_POSITION;
        mMoviesPresenter.setView(this);

        if (savedInstanceState != null) {
            mPosition = savedInstanceState.getInt("pos");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
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
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.movie_filter_title, R.layout.spinner_item);
        mSpinner.setAdapter(spinnerAdapter);
        mSpinner.setOnItemSelectedListener(this);

        mSpinner.setSelection(mSharedPreferencesRepository.getSpinnerPosition());

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
        outState.putInt("pos", mGridLayoutManager.findFirstCompletelyVisibleItemPosition());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mMoviesPresenter.unsubscribe();
        mCursor.close();
    }

    @Override
    public void showMoviesList(SqlBrite.Query query) {
        mCursor = query.run();
        mMoviesAdapter.changeCursor(mCursor);

        if (mPosition != RecyclerView.NO_POSITION) {
            mRecyclerView.scrollToPosition(mPosition);
        }
    }

    @Override
    public void showMovieDetails(long movieId) {
        mMovieItemListener.onMovieClick(movieId);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String[] filterValues = getResources().getStringArray(R.array.movie_filter_values);
        String filter = filterValues[position];

        mSharedPreferencesRepository.setFilterType(filter);
        mSharedPreferencesRepository.setSpinnerPosition(position);

        mMoviesPresenter.loadMovies(filter);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Don't do anything
    }

    public interface MovieItemListener {
        void onMovieClick(long movieId);
    }
}
