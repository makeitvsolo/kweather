package github.makeitvsolo.kweather.boot.configuration.weather.service

import github.makeitvsolo.kweather.weather.api.datasource.location.LocationRepository
import github.makeitvsolo.kweather.weather.api.datasource.weather.WeatherRepository
import github.makeitvsolo.kweather.weather.api.service.weather.WeatherService
import github.makeitvsolo.kweather.weather.api.service.weather.usecase.FetchForecast
import github.makeitvsolo.kweather.weather.api.service.weather.usecase.FetchWeather
import github.makeitvsolo.kweather.weather.application.weather.ApplicationWeatherService
import github.makeitvsolo.kweather.weather.application.weather.usecase.ApplicationFetchForecast
import github.makeitvsolo.kweather.weather.application.weather.usecase.ApplicationFetchWeather

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class WeatherServiceConfiguration {

    @Bean
    open fun fetchWeatherUsecase(
        locationRepository: LocationRepository,
        weatherRepository: WeatherRepository
    ): FetchWeather =
        ApplicationFetchWeather(locationRepository, weatherRepository)

    @Bean
    open fun fetchForecastUsecase(
        locationRepository: LocationRepository,
        weatherRepository: WeatherRepository
    ): FetchForecast =
        ApplicationFetchForecast(locationRepository, weatherRepository)

    @Bean
    open fun weatherService(
        fetchWeatherUsecase: FetchWeather,
        fetchForecastUsecase: FetchForecast
    ): WeatherService =
        ApplicationWeatherService(fetchWeatherUsecase, fetchForecastUsecase)
}
