package com.peterung.popularmovies.data.repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.peterung.popularmovies.Constants;

public class SharedPreferencesRepositoryImpl implements SharedPreferencesRepository {

    private SharedPreferences mSharedPreferences;

    public SharedPreferencesRepositoryImpl(Context context) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public String getFilterType() {
        return mSharedPreferences.getString("filter", Constants.POPULAR_FILTER);
    }

    public void setFilterType(String type) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();

        editor.putString("filter", type);
        editor.apply();
    }

    @Override
    public int getSpinnerPosition() {
        return mSharedPreferences.getInt("spinnerPosition", 0);
    }

    @Override
    public void setSpinnerPosition(int position) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt("spinnerPosition", position);
        editor.apply();
    }

}
