package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView adapter for displaying a list of Movie objects.
 * Shows placeholder values when movie data is missing or invalid.
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<Movie> movies;

    public MovieAdapter(List<Movie> movies) {
        this.movies = movies != null ? movies : new ArrayList<>();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the card layout for each movie item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movies.get(position);
        holder.bind(movie);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    /**
     * Updates the movie list and refreshes the RecyclerView.
     */
    public void updateMovies(List<Movie> newMovies) {
        this.movies = newMovies != null ? newMovies : new ArrayList<>();
        notifyDataSetChanged();
    }

    /**
     * ViewHolder for a single movie card.
     * Handles displaying movie data with placeholders for missing fields.
     */
    static class MovieViewHolder extends RecyclerView.ViewHolder {
        private ImageView posterImageView;
        private TextView titleTextView;
        private TextView yearTextView;
        private TextView genreTextView;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            posterImageView = itemView.findViewById(R.id.posterImageView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            yearTextView = itemView.findViewById(R.id.yearTextView);
            genreTextView = itemView.findViewById(R.id.genreTextView);
        }

        /**
         * Binds movie data to the views, showing placeholders for missing data.
         */
        public void bind(Movie movie) {
            // Title: show "Unknown Title" if missing
            if (movie.hasValidTitle()) {
                titleTextView.setText(movie.getTitle());
            } else {
                titleTextView.setText("Unknown Title");
            }

            // Year: show "N/A" if missing or invalid
            if (movie.hasValidYear()) {
                yearTextView.setText(String.valueOf(movie.getYear()));
            } else {
                yearTextView.setText("N/A");
            }

            // Genre: show "Unknown Genre" if missing
            if (movie.hasValidGenre()) {
                genreTextView.setText(movie.getGenre());
            } else {
                genreTextView.setText("Unknown Genre");
            }

            // Poster: show a default placeholder icon if missing
            if (movie.hasValidPoster()) {
                posterImageView.setImageResource(R.drawable.ic_movie_placeholder);
            } else {
                posterImageView.setImageResource(R.drawable.ic_movie_placeholder);
            }
        }
    }
}
