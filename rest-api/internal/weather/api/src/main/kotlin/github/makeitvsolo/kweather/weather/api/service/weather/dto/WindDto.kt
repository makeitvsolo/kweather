package github.makeitvsolo.kweather.weather.api.service.weather.dto

data class WindDto(
    val speed: Double,
    val gust: Double,
    val degree: Int,
    val direction: String
)
