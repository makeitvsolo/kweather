package github.makeitvsolo.kweather.weather.usecase.weather

import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.weather.api.datasource.location.LocationRepository
import github.makeitvsolo.kweather.weather.api.datasource.weather.WeatherRepository
import github.makeitvsolo.kweather.weather.api.service.weather.dto.DailyWeatherDto
import github.makeitvsolo.kweather.weather.api.service.weather.dto.ForecastDto
import github.makeitvsolo.kweather.weather.api.service.weather.dto.LocationDto
import github.makeitvsolo.kweather.weather.api.service.weather.usecase.FetchForecast
import github.makeitvsolo.kweather.weather.api.service.weather.usecase.FetchForecastError
import github.makeitvsolo.kweather.weather.api.service.weather.usecase.FetchForecastPayload
import github.makeitvsolo.kweather.weather.api.service.weather.usecase.FetchForecastResponse

class ApplicationFetchForecast(
    private val locationRepository: LocationRepository,
    private val weatherRepository: WeatherRepository
) : FetchForecast {

    override fun fetchForecast(payload: FetchForecastPayload): Result<FetchForecastResponse, FetchForecastError> =
        locationRepository.findFavourite(payload.accountId, payload.latitude, payload.longitude).mapError { error ->
            error.into(FetchForecastError.FromFindFavouriteError)
        }.andThen { location ->
            weatherRepository.findForecastByCoordinates(payload.latitude, payload.longitude).mapError { error ->
                error.into(FetchForecastError.FromFindForecastError)
            }.map { forecast ->
                ForecastDto(
                    location.into(LocationDto.FromLocation),
                    forecast.map { it.into(DailyWeatherDto.FromDailyWeather) }
                )
            }
        }
}
