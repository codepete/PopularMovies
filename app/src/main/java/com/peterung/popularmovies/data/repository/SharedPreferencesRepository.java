package com.peterung.popularmovies.data.repository;

public interface SharedPreferencesRepository {

    String getFilterType();
    void setFilterType(String type);
    int getSpinnerPosition();
    void setSpinnerPosition(int position);
}
