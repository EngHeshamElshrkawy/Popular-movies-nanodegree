package com.example.hesham.popularmovies.Helpers;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.hesham.popularmovies.Objects.Movie;

import java.util.List;

@Dao
public interface MovieDao {
    @Query("SELECT * FROM movies_table ORDER BY serial")
    List<Movie> loadAllMovies();

    @Insert
    void insertMovie(Movie movie);

    @Delete
    void deleteMovie(Movie movie);

    @Query("DELETE FROM movies_table WHERE id = :id")
    void deleteByUserId(int id);
}
