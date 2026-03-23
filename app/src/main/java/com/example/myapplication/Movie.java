package com.example.myapplication;

/**
 * Movie model class representing a movie with title, year, genre, and poster resource.
 * Includes validation methods to check for missing or invalid data.
 */
public class Movie {
    private String title;
    private Integer year;
    private String genre;
    private String posterResource;

    // Constructor with all fields
    public Movie(String title, Integer year, String genre, String posterResource) {
        this.title = title;
        this.year = year;
        this.genre = genre;
        this.posterResource = posterResource;
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public Integer getYear() {
        return year;
    }

    public String getGenre() {
        return genre;
    }

    public String getPosterResource() {
        return posterResource;
    }

    // Setters
    public void setTitle(String title) {
        this.title = title;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setPosterResource(String posterResource) {
        this.posterResource = posterResource;
    }

    // Validation helpers — check if each field has valid data
    public boolean hasValidTitle() {
        return title != null && !title.trim().isEmpty();
    }

    public boolean hasValidYear() {
        return year != null && year > 0;
    }

    public boolean hasValidGenre() {
        return genre != null && !genre.trim().isEmpty();
    }

    public boolean hasValidPoster() {
        return posterResource != null && !posterResource.trim().isEmpty();
    }
}
