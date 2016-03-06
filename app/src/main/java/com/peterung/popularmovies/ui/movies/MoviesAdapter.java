package com.peterung.popularmovies.ui.movies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.peterung.popularmovies.NetworkConstants;
import com.peterung.popularmovies.R;
import com.peterung.popularmovies.custom.CustomImageView;
import com.peterung.popularmovies.data.api.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {

    List<Movie> mMovies;
    MoviesPresenter mMoviesPresenter;
    Context mContext;

    public MoviesAdapter(Context context, List<Movie> movies, MoviesPresenter presenter) {
        mMovies = movies;
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
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder == null) {
            return;
        }

        Movie movie = getItem(position);

        Picasso.with(mContext)
                .load(NetworkConstants.MOVIE_DB_IMAGE_URL + "w185" + movie.posterPath)
                .placeholder(R.drawable.placeholder)
                .into(holder.movieImage);

        holder.movieTitle.setText(movie.title);
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    public Movie getItem(int pos) {
        if (mMovies == null) {
            return null;
        }

        return mMovies.get(pos);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

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
            int pos = getAdapterPosition();
            mMoviesPresenter.openMovieDetails(pos, getItem(pos));
        }
    }


    public void replaceData(List<Movie> movies) {
        mMovies = movies;
        notifyDataSetChanged();
    }
}
