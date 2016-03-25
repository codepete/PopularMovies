package com.peterung.popularmovies;

import com.peterung.popularmovies.data.sync.PopularMoviesSyncAdapter;
import com.peterung.popularmovies.ui.moviedetail.MovieDetailFragment;
import com.peterung.popularmovies.ui.movies.MainActivity;
import com.peterung.popularmovies.ui.movies.MoviesFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {

    void inject(MainActivity target);

    void inject(MoviesFragment target);

    void inject(MovieDetailFragment target);

    void inject(PopularMoviesSyncAdapter target);

}
