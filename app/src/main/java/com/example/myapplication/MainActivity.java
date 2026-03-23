package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Main activity that displays a list of movies using RecyclerView.
 * Loads movie data from a local JSON file and handles errors gracefully.
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private RecyclerView movieRecyclerView;
    private MovieAdapter adapter;
    private TextView errorTextView;
    private List<Movie> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views and load data
        setupRecyclerView();
        loadMovieData();
    }

    /**
     * Sets up the RecyclerView with a LinearLayoutManager and an empty adapter.
     */
    private void setupRecyclerView() {
        movieRecyclerView = findViewById(R.id.movieRecyclerView);
        errorTextView = findViewById(R.id.errorTextView);

        // Use a vertical linear layout for the movie list
        movieRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize adapter with an empty list
        movies = new ArrayList<>();
        adapter = new MovieAdapter(movies);
        movieRecyclerView.setAdapter(adapter);
    }

    /**
     * Loads movie data from the JSON file in assets.
     * Handles errors for missing file, invalid JSON, and bad data.
     */
    private void loadMovieData() {
        try {
            // Parse movies from JSON file
            List<Movie> loadedMovies = JsonUtils.loadMoviesFromJson(this);

            if (loadedMovies.isEmpty()) {
                // JSON was valid but no usable movie entries found
                showError("No valid movies found in the data file.");
            } else {
                // Successfully loaded movies — update the adapter
                adapter.updateMovies(loadedMovies);
                errorTextView.setVisibility(View.GONE);
                movieRecyclerView.setVisibility(View.VISIBLE);
                Log.d(TAG, "Loaded " + loadedMovies.size() + " movies successfully");
            }

        } catch (IOException e) {
            // File not found or cannot be read
            Log.e(TAG, "Failed to read JSON file: " + e.getMessage());
            JsonUtils.handleJsonException(e);
            showError("Error: Could not find or read the movie data file.");

        } catch (JSONException e) {
            // Invalid JSON format
            Log.e(TAG, "Failed to parse JSON: " + e.getMessage());
            JsonUtils.handleJsonException(e);
            showError("Error: The movie data file contains invalid JSON.");

        } catch (Exception e) {
            // Catch any unexpected errors
            Log.e(TAG, "Unexpected error: " + e.getMessage());
            showError("An unexpected error occurred while loading movies.");
        }
    }

    /**
     * Displays an error message to the user via the error TextView and a Toast.
     */
    private void showError(String message) {
        // Show error text in the layout
        errorTextView.setText(message);
        errorTextView.setVisibility(View.VISIBLE);
        movieRecyclerView.setVisibility(View.GONE);

        // Also show a brief Toast notification
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
