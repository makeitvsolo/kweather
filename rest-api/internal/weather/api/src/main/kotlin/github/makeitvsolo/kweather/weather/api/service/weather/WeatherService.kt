package github.makeitvsolo.kweather.weather.api.service.weather

import github.makeitvsolo.kweather.weather.api.service.weather.usecase.FetchForecast
import github.makeitvsolo.kweather.weather.api.service.weather.usecase.FetchWeather

interface WeatherService : FetchWeather, FetchForecast
