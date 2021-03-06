package com.peterung.popularmovies.ui.movies;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.peterung.popularmovies.Constants;
import com.peterung.popularmovies.R;
import com.peterung.popularmovies.custom.CursorRecyclerViewAdapter;
import com.peterung.popularmovies.custom.CustomImageView;
import com.peterung.popularmovies.data.provider.MovieTable;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MoviesAdapter extends CursorRecyclerViewAdapter<MoviesAdapter.ViewHolder> {

    MoviesPresenter mMoviesPresenter;
    Context mContext;

    public MoviesAdapter(Context context, Cursor cursor, MoviesPresenter presenter) {
        super(context, cursor);
        mMoviesPresenter = presenter;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_movie, parent, false);

        return new ViewHolder(view, mMoviesPresenter);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, Cursor cursor) {
        if (holder == null) {
            return;
        }

        String posterPath = cursor.getString(MovieTable.COL_POSTER_PATH);
        String title = cursor.getString(MovieTable.COL_TITLE);

        Picasso.with(mContext)
                .load(Constants.MOVIE_DB_IMAGE_URL + "w185" + posterPath)
                .placeholder(R.drawable.placeholder)
                .into(holder.movieImage);

        holder.movieTitle.setText(title);
        holder.movieId = cursor.getLong(MovieTable.COL_MOVIE_ID);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        long movieId;

        @Bind(R.id.movie_image) CustomImageView movieImage;

        @Bind(R.id.movie_title) TextView movieTitle;

        MoviesPresenter mMoviesPresenter;

        public ViewHolder(View itemView, MoviesPresenter presenter) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mMoviesPresenter = presenter;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mMoviesPresenter.openMovieDetails(movieId);
        }
    }

}
