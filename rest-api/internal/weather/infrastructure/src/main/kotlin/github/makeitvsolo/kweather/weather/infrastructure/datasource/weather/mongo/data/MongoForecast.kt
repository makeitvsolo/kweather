package github.makeitvsolo.kweather.weather.infrastructure.datasource.weather.mongo.data

internal data class MongoForecast(
    val coordinates: MongoCoordinates,
    val days: List<MongoDailyWeather>
)
