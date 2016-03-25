package com.peterung.popularmovies.ui.moviedetail;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.peterung.popularmovies.Constants;
import com.peterung.popularmovies.PopularMoviesApplication;
import com.peterung.popularmovies.R;
import com.peterung.popularmovies.custom.NoScrollLinearLayoutManager;
import com.peterung.popularmovies.data.api.MovieService;
import com.peterung.popularmovies.data.provider.MovieTable;
import com.peterung.popularmovies.data.provider.VideoTable;
import com.peterung.popularmovies.ui.movies.MainActivity;
import com.squareup.picasso.Picasso;
import com.squareup.sqlbrite.SqlBrite;

import java.util.Locale;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MovieDetailFragment extends Fragment implements MovieDetailContract.View {

    @Inject MovieService mMovieService;

    @Bind(R.id.trailers) RecyclerView mTrailersRecyclerView;

    @Bind(R.id.reviews) RecyclerView mReviewsRecyclerView;

    @Bind(R.id.movie_description_header) TextView mDescriptionHeader;

    @Bind(R.id.movie_reviews_header) TextView mReviewsHeader;

    @Bind(R.id.movie_trailers_header) TextView mTrailersHeader;

    @Bind(R.id.movie_description) TextView mDescription;

    @Bind(R.id.movie_release_date) TextView mReleaseDate;

    @Bind(R.id.backdrop) ImageView mBackdrop;

    @Nullable @Bind(R.id.poster) ImageView mPoster;

    @Bind(R.id.movie_rating) TextView mRating;

    @Bind(R.id.vote_count) TextView mVoteCount;

    @Nullable @Bind(R.id.title) TextView mTitle;

    @Nullable @Bind(R.id.toolbar) Toolbar mToolbar;

    @Nullable @Bind(R.id.collapsing_toolbar) CollapsingToolbarLayout mCollapsibleToolbar;

    @Bind(R.id.favorite_button) FloatingActionButton mFavorite;

    Cursor mReviewsCursor;
    Cursor mTrailersCursor;
    ReviewsAdapter mReviewsAdapter;
    TrailersAdapter mTrailersAdapter;
    ShareActionProvider mShareActionProvider;

    @Inject MovieDetailPresenter mMovieDetailPresenter;
    long mMovieId;
    boolean mTwoPane;
    boolean mIsFavorite;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((PopularMoviesApplication) getActivity().getApplication()).getComponent().inject(this);
        mMovieDetailPresenter.setView(this);
        mReviewsAdapter = new ReviewsAdapter(getContext(), null, mMovieDetailPresenter);
        mTrailersAdapter = new TrailersAdapter(getContext(), null, mMovieDetailPresenter);


        if (savedInstanceState != null) {
            mMovieId = savedInstanceState.getLong("movieId");
            return;
        }

        Bundle args = getArguments();
        if (args != null) {
            mMovieId = args.getLong("movieId");
            setHasOptionsMenu(true);
        } else {
            mMovieId = -1;
            setHasOptionsMenu(true);
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        mMovieDetailPresenter.loadMovieInfo(mMovieId);
        mMovieDetailPresenter.loadReviews(mMovieId);
        mMovieDetailPresenter.loadTrailers(mMovieId);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, view);

        NoScrollLinearLayoutManager reviewLayoutManager = new NoScrollLinearLayoutManager(getContext(), LinearLayout.VERTICAL, false);
        mReviewsRecyclerView.setLayoutManager(reviewLayoutManager);
        mReviewsRecyclerView.setAdapter(mReviewsAdapter);

        NoScrollLinearLayoutManager trailerLayoutManager = new NoScrollLinearLayoutManager(getContext(), LinearLayout.HORIZONTAL, false);
        mTrailersRecyclerView.setLayoutManager(trailerLayoutManager);
        mTrailersRecyclerView.setAdapter(mTrailersAdapter);


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
        outState.putLong("movieId", mMovieId);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mMovieDetailPresenter.unsubscribe();
        if (mReviewsCursor != null) {
            mReviewsCursor.close();
        }

        if (mTrailersCursor != null) {
            mTrailersCursor.close();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_movie_details, menu);

        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.menu_item_share);

        // Fetch and store ShareActionProvider
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        if (mMovieId != -1) {
            mShareActionProvider.setShareIntent(createShareMovieIntent());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.menu_item_share:
                // Update it just in case
                mShareActionProvider.setShareIntent(createShareMovieIntent());
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public Intent createShareMovieIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");

        if (mTrailersCursor != null && mTrailersCursor.getCount() > 0) {
            mTrailersCursor.moveToFirst();
            String videoKey = mTrailersCursor.getString(VideoTable.COL_KEY);
            String youTubeUrl = String.format(Constants.YOUTUBE_URL_FORMAT, videoKey);
            shareIntent.putExtra(Intent.EXTRA_TEXT, youTubeUrl);
        }
        return shareIntent;
    }

    @Override
    public void showMovieInfo(SqlBrite.Query query) {
        Cursor cursor = query.run();
        if (cursor == null) {
            return;
        }
        cursor.moveToFirst();

        mDescription.setText(cursor.getString(MovieTable.COL_OVERVIEW));
        mReleaseDate.setText(String.format(Locale.US, "Release Date: %s", cursor.getString(MovieTable.COL_RELEASE_DATE)));
        mVoteCount.setText(String.format(Locale.US, "Vote Count: %d", cursor.getInt(MovieTable.COL_VOTE_COUNT)));
        mRating.setText(String.format(Locale.US, "Vote Average: %.1f/10", cursor.getFloat(MovieTable.COL_VOTE_AVERAGE)));
        mIsFavorite = cursor.getInt(MovieTable.COL_FAVORITE) == 1;
        showFavorite(mIsFavorite);

        if (mCollapsibleToolbar != null) {
            mCollapsibleToolbar.setTitle(cursor.getString(MovieTable.COL_TITLE));
        }

        Picasso.with(getContext())
                .load(Constants.MOVIE_DB_IMAGE_URL + "w780" + cursor.getString(MovieTable.COL_BACKDROP_PATH))
                .into(mBackdrop);

        // Tablet view
        if (mPoster != null) {
            mTitle.setText(cursor.getString(MovieTable.COL_TITLE));
            Picasso.with(getContext())
                    .load(Constants.MOVIE_DB_IMAGE_URL + "w342" + cursor.getString(MovieTable.COL_POSTER_PATH))
                    .into(mPoster);
        }

        cursor.close();
    }

    @Override
    public void showReviews(SqlBrite.Query query) {
        mReviewsCursor = query.run();
        mReviewsAdapter.changeCursor(mReviewsCursor);
    }

    @Override
    public void showTrailers(SqlBrite.Query query) {
        mTrailersCursor = query.run();
        mTrailersAdapter.changeCursor(mTrailersCursor);

    }


    @Override
    public void showNoMovieSelected() {
        mDescriptionHeader.setVisibility(View.INVISIBLE);
        mFavorite.setVisibility(View.INVISIBLE);
        mReviewsHeader.setVisibility(View.INVISIBLE);
        mTrailersHeader.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showTrailer(String youtubeUrl) {
        Intent youtubeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(youtubeUrl));
        youtubeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        youtubeIntent.putExtra("force_fullscreen", true);
        startActivity(youtubeIntent);
    }

    @Override
    public void showFavorite(boolean favorite) {
        int resourceId = favorite ? R.drawable.heart_filled : R.drawable.heart_unfilled;
        mFavorite.setImageResource(resourceId);
    }

    @OnClick(R.id.favorite_button)
    public void favoriteButtonOnClick() {
        mMovieDetailPresenter.setFavorite(mMovieId, !mIsFavorite);
    }
}
