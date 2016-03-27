package com.peterung.popularmovies;

import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.peterung.popularmovies.data.api.MovieService;
import com.peterung.popularmovies.data.repository.MovieRepository;
import com.peterung.popularmovies.data.repository.MovieRepositoryImpl;
import com.peterung.popularmovies.data.repository.ReviewRepository;
import com.peterung.popularmovies.data.repository.ReviewRepositoryImpl;
import com.peterung.popularmovies.data.repository.SharedPreferencesRepository;
import com.peterung.popularmovies.data.repository.SharedPreferencesRepositoryImpl;
import com.peterung.popularmovies.data.repository.TrailerRepository;
import com.peterung.popularmovies.data.repository.TrailerRepositoryImpl;
import com.peterung.popularmovies.ui.moviedetail.MovieDetailPresenter;
import com.peterung.popularmovies.ui.movies.MoviesPresenter;
import com.peterung.popularmovies.utility.MovieDbUtility;
import com.squareup.sqlbrite.BriteContentResolver;
import com.squareup.sqlbrite.SqlBrite;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.schedulers.Schedulers;

@Module
public class ApplicationModule {
    private final Context mContext;

    public ApplicationModule(Application app) {
        this.mContext = app;
    }

    @Singleton
    @Provides
    public Context getContext() {
        return mContext;
    }

    @Singleton
    @Provides
    public SharedPreferences getSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    @Singleton
    @Provides
    public SharedPreferencesRepository getSharedPreferencesRepository() {
        return new SharedPreferencesRepositoryImpl(mContext);
    }

    @Singleton
    @Provides
    public MovieDbUtility getMovieDbHelper() {
        return new MovieDbUtility(mContext);
    }

    @Singleton
    @Provides
    public MovieService getMovieService() {

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request request = chain.request();
                    HttpUrl url = request.url().newBuilder()
                            .addQueryParameter("api_key", BuildConfig.MOVIE_DB_API_KEY)
                            .build();

                    request = request.newBuilder().url(url).build();

                    return chain.proceed(request);
                })
                .addInterceptor(new StethoInterceptor())
                .build();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.MOVIE_DB_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();

        return retrofit.create(MovieService.class);
    }

    @Singleton
    @Provides
    public ContentResolver getContentResolver(Context context) {
        return context.getContentResolver();
    }

    @Singleton
    @Provides
    public BriteContentResolver getBriteContentResolver(ContentResolver contentResolver) {
        SqlBrite sqlBrite = SqlBrite.create();
        return sqlBrite.wrapContentProvider(contentResolver, Schedulers.io());
    }

    @Singleton
    @Provides
    public MovieRepository getMovieRespository(BriteContentResolver briteContentResolver, ContentResolver contentResolver) {
        return new MovieRepositoryImpl(briteContentResolver, contentResolver);
    }

    @Singleton
    @Provides
    public ReviewRepository getReviewRepository(BriteContentResolver briteContentResolver) {
        return new ReviewRepositoryImpl(briteContentResolver);
    }

    @Singleton
    @Provides
    public TrailerRepository getTrailerRepository(BriteContentResolver briteContentResolver) {
        return new TrailerRepositoryImpl(briteContentResolver);
    }

    @Provides
    public MoviesPresenter getMoviesPresenter(MovieRepository movieRepository, MovieDbUtility movieDbUtility) {
        return new MoviesPresenter(movieRepository, movieDbUtility);
    }

    @Provides
    public MovieDetailPresenter getMovieDetailPresenter(MovieRepository movieRepository,
                                                        TrailerRepository trailerRepository,
                                                        ReviewRepository reviewRepository,
                                                        MovieDbUtility movieDbUtility) {
        return new MovieDetailPresenter(movieRepository, reviewRepository, trailerRepository, movieDbUtility);
    }
}
