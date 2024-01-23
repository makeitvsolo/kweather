package github.makeitvsolo.kweather.weather.infrastructure.datasource.weather.mongo.data

import github.makeitvsolo.kweather.weather.domain.weather.forecast.DailyWeather
import github.makeitvsolo.kweather.weather.domain.weather.forecast.MapDailyWeatherInto
import github.makeitvsolo.kweather.weather.domain.weather.forecast.value.DailyHumidity
import github.makeitvsolo.kweather.weather.domain.weather.forecast.value.DailyPrecipitation
import github.makeitvsolo.kweather.weather.domain.weather.forecast.value.DailyTemperature
import github.makeitvsolo.kweather.weather.domain.weather.forecast.value.DailyWind

import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Filters.eq
import org.bson.Document
import org.bson.conversions.Bson

import java.math.BigDecimal
import java.time.LocalDate

internal object ForecastDocument : MapDailyWeatherInto<Document> {

    fun fetchByCoordinates(latitude: BigDecimal, longitude: BigDecimal): Bson =
        and(eq("latitude", latitude), eq("longitude", longitude))

    fun from(
        latitude: BigDecimal,
        longitude: BigDecimal,
        forecast: List<DailyWeather>
    ): Document =
        Document().also { document ->
            document["latitude"] = latitude
            document["longitude"] = longitude
            document["days"] = forecast.map { day -> day.into(this) }
        }

    override fun from(
        code: Int,
        summary: String,
        date: LocalDate,
        temperature: DailyTemperature,
        wind: DailyWind,
        humidity: DailyHumidity,
        precipitation: DailyPrecipitation
    ): Document =
        Document().also { document ->
            document["code"] = code
            document["summary"] = summary
            document["date"] = date.toString()
            document["avg_temp"] = temperature.average
            document["max_temp"] = temperature.max
            document["min_temp"] = temperature.min
            document["wind"] = wind.speed
            document["humidity"] = humidity.average
            document["precipitation"] = precipitation.amount
            document["rain_chance"] = precipitation.rainChance
            document["snow_chance"] = precipitation.snowChance
        }
}

internal fun Document.intoDailyWeather(): DailyWeather =
    DailyWeather.from(
        this["code"] as Int,
        this["summary"] as String,
        LocalDate.parse(this["date"] as String),
        DailyTemperature(
            this["avg_temp"] as Double,
            this["max_temp"] as Double,
            this["min_temp"] as Double
        ),
        DailyWind(this["wind"] as Double),
        DailyHumidity(this["humidity"] as Int),
        DailyPrecipitation(
            this["precipitation"] as Double,
            this["rain_chance"] as Int,
            this["snow_chance"] as Int
        )
    )

internal fun Document.intoForecast(): List<DailyWeather> {
    val days = this["days"] as List<*>

    return days.map {
        val day = it as Document
        day.intoDailyWeather()
    }
}
