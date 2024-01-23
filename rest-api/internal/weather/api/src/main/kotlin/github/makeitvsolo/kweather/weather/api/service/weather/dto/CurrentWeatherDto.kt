package github.makeitvsolo.kweather.weather.api.service.weather.dto

data class CurrentWeatherDto(
    val location: LocationDto,
    val weather: WeatherDto
)
