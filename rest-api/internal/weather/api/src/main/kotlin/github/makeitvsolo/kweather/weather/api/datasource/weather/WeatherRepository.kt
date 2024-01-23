package github.makeitvsolo.kweather.weather.api.datasource.weather

import github.makeitvsolo.kweather.weather.api.datasource.weather.operation.FindForecast
import github.makeitvsolo.kweather.weather.api.datasource.weather.operation.FindWeather

interface WeatherRepository : FindWeather, FindForecast
