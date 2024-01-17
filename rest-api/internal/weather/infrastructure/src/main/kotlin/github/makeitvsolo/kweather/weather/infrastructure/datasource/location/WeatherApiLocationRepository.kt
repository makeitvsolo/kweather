package github.makeitvsolo.kweather.weather.infrastructure.datasource.location

import github.makeitvsolo.kweather.core.error.handling.Result

import com.github.kittinunf.fuel.gson.responseObject
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result.Failure
import com.github.kittinunf.result.Result.Success

import java.math.BigDecimal

data class WeatherApiLocation(
    val id: Int,
    val name: String,
    val region: String,
    val country: String,
    val lat: BigDecimal,
    val lon: BigDecimal,
    val url: String
)

data class SearchByNameError(private val throwable: Throwable)

sealed interface SearchByCoordinatesError {

    data class NotFoundError(private val details: String) : SearchByCoordinatesError

    data class InternalError(private val throwable: Throwable) : SearchByCoordinatesError
}

class WeatherApiLocationRepository internal constructor(
    private val apiKey: String
) {

    fun searchByName(name: String): Result<List<WeatherApiLocation>, SearchByNameError> {
        val (_, _, result) = API_URL.httpGet(listOf(KEY_PARAMETER to apiKey, QUERY_PARAMETER to name))
            .responseObject<List<WeatherApiLocation>>()

        return when (result) {
            is Success -> Result.ok(result.get())

            is Failure -> Result.error(SearchByNameError(result.getException()))
        }
    }

    fun searchByCoordinates(latitude: BigDecimal, longitude: BigDecimal):
    Result<WeatherApiLocation, SearchByCoordinatesError> {
        val (_, _, result) = API_URL.httpGet(listOf(KEY_PARAMETER to apiKey, QUERY_PARAMETER to "$latitude,$longitude"))
            .responseObject<List<WeatherApiLocation>>()

        return when (result) {
            is Success -> {
                val locations = result.get()

                if (locations.isNotEmpty()) {
                    Result.ok(locations.first())
                } else {
                    Result.error(SearchByCoordinatesError.NotFoundError("location not found"))
                }
            }

            is Failure -> Result.error(SearchByCoordinatesError.InternalError(result.getException()))
        }
    }

    companion object {

        private const val API_URL = "http://api.weatherapi.com/v1/search.json"
        private const val KEY_PARAMETER = "key"
        private const val QUERY_PARAMETER = "q"
    }
}
