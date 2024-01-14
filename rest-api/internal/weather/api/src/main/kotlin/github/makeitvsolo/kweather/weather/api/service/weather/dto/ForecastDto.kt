package github.makeitvsolo.kweather.weather.api.service.weather.dto

data class ForecastDto(
    val location: LocationDto,
    val forecast: List<DailyWeatherDto>
)
