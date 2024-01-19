package github.makeitvsolo.kweather.weather.infrastructure.datasource.weather.mongo

import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.weather.api.datasource.weather.error.FindForecastError
import github.makeitvsolo.kweather.weather.api.datasource.weather.operation.FindForecast
import github.makeitvsolo.kweather.weather.domain.weather.forecast.DailyWeather
import github.makeitvsolo.kweather.weather.domain.weather.forecast.value.DailyHumidity
import github.makeitvsolo.kweather.weather.domain.weather.forecast.value.DailyPrecipitation
import github.makeitvsolo.kweather.weather.domain.weather.forecast.value.DailyTemperature
import github.makeitvsolo.kweather.weather.domain.weather.forecast.value.DailyWind
import github.makeitvsolo.kweather.weather.infrastructure.datasource.weather.mongo.data.MongoCoordinates
import github.makeitvsolo.kweather.weather.infrastructure.datasource.weather.mongo.data.MongoDailyWeather
import github.makeitvsolo.kweather.weather.infrastructure.datasource.weather.mongo.data.MongoForecast
import github.makeitvsolo.kweather.weather.infrastructure.datasource.weather.mongo.error.CreateCollectionError
import github.makeitvsolo.kweather.weather.infrastructure.datasource.weather.mongo.error.SaveForecastError
import github.makeitvsolo.kweather.weather.infrastructure.datasource.weather.mongo.error.TruncateCollectionError

import com.google.gson.Gson
import com.mongodb.BasicDBObject
import com.mongodb.MongoException
import com.mongodb.client.MongoDatabase
import org.bson.Document

import java.math.BigDecimal

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
            return Result.error(SaveForecastError(ex))
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

    fun truncateCollection(): Result<Unit, TruncateCollectionError> {
        try {
            val collection = datasource.getCollection(FORECAST_COLLECTION)
            collection.deleteMany(BasicDBObject())

            return Result.ok(Unit)
        } catch (ex: MongoException) {
            return Result.error(TruncateCollectionError(ex))
        }
    }

    companion object {

        private const val FORECAST_COLLECTION = "forecast"
    }
}
