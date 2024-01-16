package github.makeitvsolo.kweather.weather.application.usecase.weather

import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.weather.api.datasource.location.LocationRepository
import github.makeitvsolo.kweather.weather.api.datasource.weather.WeatherRepository
import github.makeitvsolo.kweather.weather.api.service.weather.dto.CurrentWeatherDto
import github.makeitvsolo.kweather.weather.api.service.weather.dto.LocationDto
import github.makeitvsolo.kweather.weather.api.service.weather.dto.WeatherDto
import github.makeitvsolo.kweather.weather.api.service.weather.usecase.FetchWeather
import github.makeitvsolo.kweather.weather.api.service.weather.usecase.FetchWeatherError
import github.makeitvsolo.kweather.weather.api.service.weather.usecase.FetchWeatherPayload
import github.makeitvsolo.kweather.weather.api.service.weather.usecase.FetchWeatherResponse

class ApplicationFetchWeather(
    private val locationRepository: LocationRepository,
    private val weatherRepository: WeatherRepository
) : FetchWeather {

    override fun fetchWeather(payload: FetchWeatherPayload): Result<FetchWeatherResponse, FetchWeatherError> =
        locationRepository.findFavourite(payload.accountId, payload.latitude, payload.longitude).mapError { error ->
            error.into(FetchWeatherError.FromFindFavouriteError)
        }.andThen { location ->
            weatherRepository.findByCoordinates(payload.latitude, payload.longitude).mapError { error ->
                error.into(FetchWeatherError.FromFindWeatherError)
            }.map { weather ->
                CurrentWeatherDto(
                    location.into(LocationDto.FromLocation),
                    weather.into(WeatherDto.FromWeather)
                )
            }
        }
}
