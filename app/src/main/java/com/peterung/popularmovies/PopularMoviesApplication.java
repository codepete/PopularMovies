package com.peterung.popularmovies;

import android.app.Application;

public class PopularMoviesApplication extends Application {

    private ApplicationComponent mComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();

    }


    public ApplicationComponent getComponent() {
        return mComponent;
    }
}
