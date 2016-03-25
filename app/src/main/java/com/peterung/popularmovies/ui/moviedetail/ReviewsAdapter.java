package com.peterung.popularmovies.ui.moviedetail;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.peterung.popularmovies.R;
import com.peterung.popularmovies.custom.CursorRecyclerViewAdapter;
import com.peterung.popularmovies.data.provider.ReviewTable;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ReviewsAdapter extends CursorRecyclerViewAdapter<ReviewsAdapter.ViewHolder>{

    MovieDetailPresenter mMovieDetailPresenter;
    Context mContext;


    public ReviewsAdapter(Context context, Cursor cursor, MovieDetailPresenter presenter) {
        super(context, cursor);
        mMovieDetailPresenter = presenter;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_review, parent, false);

        return new ViewHolder(view, mMovieDetailPresenter);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, Cursor cursor) {
        if (holder == null) {
            return;
        }

        String content = cursor.getString(ReviewTable.COL_CONTENT).trim();
        String author = cursor.getString(ReviewTable.COL_AUTHOR);

        holder.reviewContent.setText(content);
        holder.reviewAuthor.setText(author);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Bind(R.id.content)
        TextView reviewContent;

        @Bind(R.id.author)
        TextView reviewAuthor;

        MovieDetailPresenter mMovieDetailPresenter;

        public ViewHolder(View itemView, MovieDetailPresenter presenter) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mMovieDetailPresenter = presenter;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // Do nothing...
        }
    }
}
