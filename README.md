# Movie Database App

An Android application that displays a list of movies from a local JSON file using RecyclerView, with robust error handling for invalid data.

## Features

- Displays movies in a scrollable list using **RecyclerView** with card-based layout
- Reads and parses movie data from a local **JSON file** (`movies.json`)
- Handles various data errors gracefully without crashing:
  - Missing or null fields (title, year, genre, poster)
  - Invalid year formats (strings, negative numbers, decimals)
  - Empty JSON objects
  - Malformed JSON files
- Shows **placeholder values** for missing data ("Unknown Title", "N/A", "Unknown Genre")
- User-friendly **error messages** when data cannot be loaded

## Project Structure

```
app/src/main/java/com/example/myapplication/
├── Movie.java          # Movie model class with validation
├── JsonUtils.java      # JSON parsing and error handling utility
├── MovieAdapter.java   # RecyclerView adapter with ViewHolder
└── MainActivity.java   # Main activity with RecyclerView setup

app/src/main/res/layout/
├── activity_main.xml   # Main layout with RecyclerView
└── item_movie.xml      # Card layout for each movie item

app/src/main/assets/
└── movies.json         # Movie data with intentional errors
```

## Error Handling

The app handles the following error cases from the provided `movies.json`:

| Movie | Error | How It's Handled |
|-------|-------|-----------------|
| Interstellar | Year as string `"2014"` | Parsed to integer |
| Entry #4 | Null title | Shows "Unknown Title" |
| Pulp Fiction | Non-numeric year `"nineteen-ninety-four"` | Shows "N/A" |
| Avatar | Missing genre | Shows "Unknown Genre" |
| Titanic | Negative year `-1997` | Shows "N/A" |
| Jurassic Park | Null poster | Shows placeholder icon |
| Entry #9 | Empty object `{}` | Skipped entirely |
| The Shawshank Redemption | Only has title | Shows placeholders for missing fields |
| The Godfather | Decimal year `1972.5` | Shows "N/A" |

## Tech Stack

- **Language:** Java
- **UI:** RecyclerView, MaterialCardView, ConstraintLayout
- **Data:** Local JSON parsing with `org.json`
- **Min SDK:** API 24 (Android 7.0)

## How to Run

1. Open the project in Android Studio
2. Sync Gradle dependencies
3. Run on an emulator or physical device
