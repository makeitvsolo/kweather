package github.makeitvsolo.kweather.weather.infrastructure.datasource.weather.mongo.data

internal data class MongoForecast(
    val latitude: String,
    val longitude: String,
    val days: List<MongoDailyWeather>
)
