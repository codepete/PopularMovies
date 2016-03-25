package com.peterung.popularmovies;

import android.app.Application;

import com.facebook.stetho.Stetho;

public class PopularMoviesApplication extends Application {

    private ApplicationComponent mComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();

        Stetho.initializeWithDefaults(this);

    }


    public ApplicationComponent getComponent() {
        return mComponent;
    }
}
