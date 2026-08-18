# TODOList

A TODO list app with live weather context (current temperature, sunrise/sunset), built with
Kotlin, Jetpack Compose, and a modularized clean architecture (MVVM).

## Setup

1. Get a free API key from [weatherapi.com](https://www.weatherapi.com/) (Sign Up → free plan).
2. Add it to `local.properties` (not checked into version control):

   ```
   WEATHER_API_KEY=your_key_here
   ```

3. Build and run. On first launch the app requests location permission to fetch local weather;
   the todo list works regardless of whether it's granted.

## Architecture

Modularized clean architecture, each feature split into `domain` / `data` / `presentation`:

- `core:common` — cross-cutting utilities (`AppResult`, `DispatcherProvider`).
- `core:ui` — Material 3 theme (light/dark + dynamic color), shared composables.
- `core:database` — Room (`TaskEntity`, `TaskDao`, `AppDatabase`).
- `core:network` — generic Retrofit/OkHttp networking layer (`RetrofitFactory`, `safeApiCall`).
- `core:location` — `FusedLocationProviderClient` wrapper.
- `feature:todo` — add/complete/delete tasks, persisted locally via Room.
- `feature:weather` — current temperature, sunrise, sunset for the device's location, via
  [WeatherAPI](https://www.weatherapi.com/docs/) `forecast.json`.
- `app` — Hilt wiring, `MainActivity`, permission flow, screen composition.

Dependency injection via Hilt; navigation between the weather header and the todo list is
composed directly (single-screen app) rather than through Navigation Compose, since there's
only one destination.

## Tests

Unit tests cover use cases, repositories, mappers, and the `TodoViewModel` across
`core:common`, `feature:todo`, and `feature:weather`, using JUnit4, MockK, Turbine, and Truth.

```
./gradlew test
```
