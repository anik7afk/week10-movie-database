package com.example.myapplication;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for reading and parsing the local movies.json file.
 * Handles file I/O errors, JSON parsing errors, and invalid data gracefully.
 */
public class JsonUtils {

    private static final String TAG = "JsonUtils";
    private static final String FILE_NAME = "movies.json";

    /**
     * Loads movies from the JSON file in the assets folder.
     * Skips invalid entries and logs warnings for problematic data.
     *
     * @param context the application context for accessing assets
     * @return list of parsed Movie objects (may be empty if all entries are invalid)
     * @throws IOException   if the JSON file cannot be found or read
     * @throws JSONException if the JSON file is not valid JSON
     */
    public static List<Movie> loadMoviesFromJson(Context context) throws IOException, JSONException {
        List<Movie> movies = new ArrayList<>();

        // Read the JSON file from assets
        String jsonString = readJsonFromAssets(context);

        // Parse the JSON array
        JSONArray jsonArray = new JSONArray(jsonString);
        Log.d(TAG, "Found " + jsonArray.length() + " entries in JSON file");

        // Process each movie entry
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                // Skip completely empty objects
                if (jsonObject.length() == 0) {
                    Log.w(TAG, "Entry " + i + ": Skipping empty object");
                    continue;
                }

                // Parse each field with error handling
                Movie movie = parseMovie(jsonObject, i);
                if (movie != null) {
                    movies.add(movie);
                }
            } catch (JSONException e) {
                Log.e(TAG, "Entry " + i + ": Error parsing JSON object - " + e.getMessage());
            }
        }

        Log.d(TAG, "Successfully loaded " + movies.size() + " movies");
        return movies;
    }

    /**
     * Reads the raw JSON string from the assets folder.
     */
    private static String readJsonFromAssets(Context context) throws IOException {
        InputStream inputStream = context.getAssets().open(FILE_NAME);
        int size = inputStream.available();
        byte[] buffer = new byte[size];
        inputStream.read(buffer);
        inputStream.close();
        return new String(buffer, StandardCharsets.UTF_8);
    }

    /**
     * Parses a single JSONObject into a Movie, handling invalid/missing fields.
     * Returns null only if the entry has no usable data at all.
     */
    private static Movie parseMovie(JSONObject jsonObject, int index) {
        // Parse title (may be null or missing)
        String title = null;
        if (jsonObject.has("title") && !jsonObject.isNull("title")) {
            title = jsonObject.optString("title", null);
        }
        if (title == null) {
            Log.w(TAG, "Entry " + index + ": Missing or null title");
        }

        // Parse year — handle strings, decimals, negatives
        Integer year = parseYear(jsonObject, index);

        // Parse genre (may be missing)
        String genre = null;
        if (jsonObject.has("genre") && !jsonObject.isNull("genre")) {
            genre = jsonObject.optString("genre", null);
        }
        if (genre == null) {
            Log.w(TAG, "Entry " + index + ": Missing or null genre");
        }

        // Parse poster resource (may be null or missing)
        String poster = null;
        if (jsonObject.has("poster") && !jsonObject.isNull("poster")) {
            poster = jsonObject.optString("poster", null);
        }
        if (poster == null) {
            Log.w(TAG, "Entry " + index + ": Missing or null poster");
        }

        // Create movie even with partial data — the adapter will show placeholders
        return new Movie(title, year, genre, poster);
    }

    /**
     * Parses the year field, handling various error cases:
     * - Year as string number ("2014") -> parse to integer
     * - Year as non-numeric string ("nineteen-ninety-four") -> null
     * - Year as negative number (-1997) -> null
     * - Year as decimal (1972.5) -> null
     * - Year missing -> null
     */
    private static Integer parseYear(JSONObject jsonObject, int index) {
        if (!jsonObject.has("year") || jsonObject.isNull("year")) {
            Log.w(TAG, "Entry " + index + ": Missing year field");
            return null;
        }

        // Get the raw value to check its type
        Object yearValue = jsonObject.opt("year");

        // Handle string years
        if (yearValue instanceof String) {
            String yearStr = (String) yearValue;
            try {
                int parsed = Integer.parseInt(yearStr);
                if (parsed <= 0) {
                    Log.w(TAG, "Entry " + index + ": Negative year value '" + yearStr + "'");
                    return null;
                }
                Log.w(TAG, "Entry " + index + ": Year was a string '" + yearStr + "', converted to integer");
                return parsed;
            } catch (NumberFormatException e) {
                Log.w(TAG, "Entry " + index + ": Non-numeric year string '" + yearStr + "'");
                return null;
            }
        }

        // Handle numeric years (int or double)
        if (yearValue instanceof Double) {
            double doubleYear = (Double) yearValue;
            // Check if it's a whole number
            if (doubleYear != Math.floor(doubleYear)) {
                Log.w(TAG, "Entry " + index + ": Decimal year value '" + doubleYear + "'");
                return null;
            }
            if (doubleYear <= 0) {
                Log.w(TAG, "Entry " + index + ": Negative year value '" + doubleYear + "'");
                return null;
            }
            return (int) doubleYear;
        }

        if (yearValue instanceof Integer) {
            int intYear = (Integer) yearValue;
            if (intYear <= 0) {
                Log.w(TAG, "Entry " + index + ": Negative year value '" + intYear + "'");
                return null;
            }
            return intYear;
        }

        // Handle other numeric types (Long, etc.)
        try {
            int intYear = jsonObject.getInt("year");
            if (intYear <= 0) {
                Log.w(TAG, "Entry " + index + ": Negative year value '" + intYear + "'");
                return null;
            }
            return intYear;
        } catch (JSONException e) {
            Log.w(TAG, "Entry " + index + ": Invalid year format - " + e.getMessage());
            return null;
        }
    }

    /**
     * Logs details about a JSON-related exception for debugging.
     */
    public static void handleJsonException(Exception e) {
        Log.e(TAG, "JSON Error: " + e.getClass().getSimpleName() + " - " + e.getMessage());
    }
}
