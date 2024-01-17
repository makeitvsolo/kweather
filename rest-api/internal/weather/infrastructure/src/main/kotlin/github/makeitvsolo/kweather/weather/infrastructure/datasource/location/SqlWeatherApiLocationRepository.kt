package github.makeitvsolo.kweather.weather.infrastructure.datasource.location

import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.weather.api.datasource.location.LocationRepository
import github.makeitvsolo.kweather.weather.api.datasource.location.operation.AddFavouriteError
import github.makeitvsolo.kweather.weather.api.datasource.location.operation.FindFavouriteError
import github.makeitvsolo.kweather.weather.api.datasource.location.operation.RemoveFavouriteError
import github.makeitvsolo.kweather.weather.api.datasource.location.operation.SearchLocationError
import github.makeitvsolo.kweather.weather.domain.location.Location

import java.math.BigDecimal

class SqlWeatherApiLocationRepository internal constructor(
    private val sql: SqlLocationRepository,
    private val weatherApi: WeatherApiLocationRepository
) : LocationRepository {

    override fun addFavourite(
        accountId: String,
        latitude: BigDecimal,
        longitude: BigDecimal
    ): Result<Unit, AddFavouriteError> =
        sql.addFavourite(accountId, latitude, longitude)

    override fun removeFavourite(
        accountId: String,
        latitude: BigDecimal,
        longitude: BigDecimal
    ): Result<Unit, RemoveFavouriteError> =
        sql.removeFavourite(accountId, latitude, longitude)

    override fun findFavourite(
        accountId: String,
        latitude: BigDecimal,
        longitude: BigDecimal
    ): Result<Location, FindFavouriteError> =
        sql.existsFavourite(accountId, latitude, longitude).mapError<FindFavouriteError> { error ->
            FindFavouriteError.InternalError(error.intoThrowable())
        }.andThen { exists ->
            if (exists) {
                return@andThen weatherApi.searchByCoordinates(latitude, longitude).map<Location> { location ->
                    Location.asFavourite(
                        location.name,
                        location.region,
                        location.country,
                        latitude,
                        longitude
                    )
                }.mapError<FindFavouriteError> { error ->
                    FindFavouriteError.InternalError(error.intoThrowable())
                }
            }

            return@andThen Result.error(FindFavouriteError.NotFoundError("location does not exists"))
        }

    override fun findAllFavourite(accountId: String): Result<List<Location>, FindFavouriteError> =
        sql.findFavouriteCoordinates(accountId).mapError<FindFavouriteError> { error ->
            FindFavouriteError.InternalError(error.intoThrowable())
        }.andThen { coordinates ->
            val locations: MutableList<Location> = mutableListOf()

            for (coordinate in coordinates) {
                val result = weatherApi.searchByCoordinates(coordinate.latitude, coordinate.longitude)

                if (result.isError) {
                    val error = result.unwrapError()
                    return@andThen Result.error(FindFavouriteError.InternalError(error.intoThrowable()))
                }

                val response = result.unwrap()
                locations.add(
                    Location.asFavourite(
                        response.name,
                        response.region,
                        response.country,
                        coordinate.latitude,
                        coordinate.longitude
                    )
                )
            }

            return@andThen Result.ok(locations)
        }

    override fun searchByName(accountId: String, name: String): Result<List<Location>, SearchLocationError> =
        weatherApi.searchByName(name).mapError<SearchLocationError> { error ->
            SearchLocationError.InternalError(error.intoThrowable())
        }.andThen { response ->
            val locations: MutableList<Location> = mutableListOf()

            for (location in response) {
                val result = sql.existsFavourite(accountId, location.latitude, location.longitude)

                if (result.isError) {
                    val error = result.unwrapError()
                    return@andThen Result.error(SearchLocationError.InternalError(error.intoThrowable()))
                }

                val exists = result.unwrap()
                locations.add(
                    Location.from(
                        location.name,
                        location.region,
                        location.country,
                        location.latitude,
                        location.longitude,
                        isFavourite = exists
                    )
                )
            }

            return@andThen Result.ok(locations)
        }
}
