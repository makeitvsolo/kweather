package github.makeitvsolo.kweather.weather.infrastructure.datasource.weather.mongo

import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.weather.api.datasource.weather.error.FindForecastError
import github.makeitvsolo.kweather.weather.api.datasource.weather.operation.FindForecast
import github.makeitvsolo.kweather.weather.domain.weather.forecast.DailyWeather
import github.makeitvsolo.kweather.weather.infrastructure.datasource.weather.mongo.data.ForecastDocument
import github.makeitvsolo.kweather.weather.infrastructure.datasource.weather.mongo.data.intoForecast
import github.makeitvsolo.kweather.weather.infrastructure.datasource.weather.mongo.error.CreateCollectionError
import github.makeitvsolo.kweather.weather.infrastructure.datasource.weather.mongo.error.SaveForecastError
import github.makeitvsolo.kweather.weather.infrastructure.datasource.weather.mongo.error.TruncateCollectionError

import com.mongodb.BasicDBObject
import com.mongodb.MongoException
import com.mongodb.client.MongoDatabase

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

            collection.insertOne(ForecastDocument.from(latitude, longitude, forecast))

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

            val cursor = collection.find(
                ForecastDocument.fetchByCoordinates(latitude, longitude)
            ).cursor()

            cursor.use { documents ->
                if (documents.hasNext()) {
                    val document = documents.next()

                    return Result.ok(
                        document.intoForecast()
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
