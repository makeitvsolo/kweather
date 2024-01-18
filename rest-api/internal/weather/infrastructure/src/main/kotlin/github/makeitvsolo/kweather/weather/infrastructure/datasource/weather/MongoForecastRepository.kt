package github.makeitvsolo.kweather.weather.infrastructure.datasource.weather

import github.makeitvsolo.kweather.core.error.handling.IntoThrowable
import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.weather.api.datasource.weather.operation.FindForecast
import github.makeitvsolo.kweather.weather.api.datasource.weather.operation.FindForecastError
import github.makeitvsolo.kweather.weather.domain.weather.forecast.DailyWeather
import github.makeitvsolo.kweather.weather.domain.weather.forecast.MapDailyWeatherInto
import github.makeitvsolo.kweather.weather.domain.weather.forecast.value.DailyHumidity
import github.makeitvsolo.kweather.weather.domain.weather.forecast.value.DailyPrecipitation
import github.makeitvsolo.kweather.weather.domain.weather.forecast.value.DailyTemperature
import github.makeitvsolo.kweather.weather.domain.weather.forecast.value.DailyWind

import com.google.gson.Gson
import com.mongodb.BasicDBObject
import com.mongodb.MongoException
import com.mongodb.client.MongoDatabase
import org.bson.Document

import java.math.BigDecimal
import java.time.LocalDate

sealed interface SaveForecastError : IntoThrowable {

    data class NotFoundError(private val details: String) : SaveForecastError {

        override fun intoThrowable(): Throwable = Throwable(details)
    }

    data class InternalError(private val throwable: Throwable) : SaveForecastError {

        override fun intoThrowable(): Throwable = throwable
    }
}

data class CreateCollectionError internal constructor(private val throwable: Throwable) : IntoThrowable {

    override fun intoThrowable(): Throwable = throwable
}

data class DropCollectionError internal constructor(private val throwable: Throwable) : IntoThrowable {

    override fun intoThrowable(): Throwable = throwable
}

data class MongoCoordinates(
    val latitude: BigDecimal,
    val longitude: BigDecimal
)

data class MongoDailyWeather(
    val code: Int,
    val summary: String,
    val date: LocalDate,
    val averageTemperature: Double,
    val maxTemperature: Double,
    val minTemperature: Double,
    val wind: Double,
    val humidity: Int,
    val precipitation: Double,
    val rainChance: Int,
    val snowChance: Int
) {

    object FromDailyWeather : MapDailyWeatherInto<MongoDailyWeather> {

        override fun from(
            code: Int,
            summary: String,
            date: LocalDate,
            temperature: DailyTemperature,
            wind: DailyWind,
            humidity: DailyHumidity,
            precipitation: DailyPrecipitation
        ): MongoDailyWeather =
            MongoDailyWeather(
                code,
                summary,
                date,
                temperature.average,
                temperature.max,
                temperature.min,
                wind.speed,
                humidity.average,
                precipitation.amount,
                precipitation.rainChance,
                precipitation.snowChance
            )
    }
}

data class MongoForecast(
    val coordinates: MongoCoordinates,
    val days: List<MongoDailyWeather>
)

class MongoForecastRepository internal constructor(
    private val datasource: MongoDatabase
) : FindForecast {

    fun saveForecast(
        latitude: BigDecimal,
        longitude: BigDecimal,
        forecast: List<DailyWeather>
    ): Result<Unit, SaveForecastError> {
        try {
            val collection = datasource.getCollection(FORECAST_COLLECTION)

            val mongoForecast = MongoForecast(
                MongoCoordinates(latitude, longitude),
                forecast.map { it.into(MongoDailyWeather.FromDailyWeather) }
            )

            collection.insertOne(Document.parse(Gson().toJson(mongoForecast)))
            return Result.ok(Unit)
        } catch (ex: MongoException) {
            return Result.error(SaveForecastError.InternalError(ex))
        }
    }

    override fun findForecastByCoordinates(
        latitude: BigDecimal,
        longitude: BigDecimal
    ): Result<List<DailyWeather>, FindForecastError> {
        try {
            val collection = datasource.getCollection(FORECAST_COLLECTION)
            val query = Document().also {
                it["coordinates"] = MongoCoordinates(latitude, longitude)
            }

            val cursor = collection.find(query).cursor()

            cursor.use { documents ->
                if (documents.hasNext()) {
                    val mongoForecast = Gson().fromJson(documents.next().toJson(), MongoForecast::class.java)

                    return Result.ok(
                        mongoForecast.days.map { day ->
                            DailyWeather.from(
                                day.code,
                                day.summary,
                                day.date,
                                DailyTemperature(
                                    day.averageTemperature,
                                    day.maxTemperature,
                                    day.minTemperature
                                ),
                                DailyWind(day.wind),
                                DailyHumidity(day.humidity),
                                DailyPrecipitation(
                                    day.precipitation,
                                    day.rainChance,
                                    day.snowChance
                                )
                            )
                        }
                    )
                }

                return Result.error(FindForecastError.NotFoundError("forecast not found"))
            }
        } catch (ex: MongoException) {
            return Result.error(FindForecastError.InternalError(ex))
        }
    }

    fun createCollection(): Result<Unit, CreateCollectionError> {
        try {
            datasource.createCollection(FORECAST_COLLECTION)
            return Result.ok(Unit)
        } catch (ex: MongoException) {
            return Result.error(CreateCollectionError(ex))
        }
    }

    fun dropCollection(): Result<Unit, DropCollectionError> {
        try {
            val collection = datasource.getCollection(FORECAST_COLLECTION)
            collection.deleteMany(BasicDBObject())

            return Result.ok(Unit)
        } catch (ex: MongoException) {
            return Result.error(DropCollectionError(ex))
        }
    }

    companion object {

        private const val FORECAST_COLLECTION = "forecast"
    }
}
