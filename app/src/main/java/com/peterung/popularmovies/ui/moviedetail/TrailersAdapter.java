package com.peterung.popularmovies.ui.moviedetail;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.peterung.popularmovies.Constants;
import com.peterung.popularmovies.R;
import com.peterung.popularmovies.custom.CursorRecyclerViewAdapter;
import com.peterung.popularmovies.data.provider.VideoTable;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TrailersAdapter extends CursorRecyclerViewAdapter<TrailersAdapter.ViewHolder> {

    MovieDetailPresenter mMovieDetailPresenter;
    Context mContext;

    public TrailersAdapter(Context context, Cursor cursor, MovieDetailPresenter presenter) {
        super(context, cursor);
        mMovieDetailPresenter = presenter;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_trailer, parent, false);

        return new ViewHolder(view, mMovieDetailPresenter);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, Cursor cursor) {
        if (holder == null) {
            return;
        }

        String videoId = cursor.getString(VideoTable.COL_KEY);

        Picasso.with(mContext)
                .load(String.format(Constants.YOUTUBE_IMAGE_URL_FORMAT, videoId))
                .placeholder(R.drawable.placeholder)
                .into(holder.trailerImage);

        holder.trailerKey = cursor.getString(VideoTable.COL_KEY);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Bind(R.id.trailer)
        ImageView trailerImage;
        String trailerKey;

        MovieDetailPresenter mMovieDetailPresenter;

        public ViewHolder(View itemView, MovieDetailPresenter presenter) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mMovieDetailPresenter = presenter;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mMovieDetailPresenter.playTrailer(trailerKey);
        }
    }
}
