package github.makeitvsolo.kweather.weather.infrastructure.datasource.location.sql

import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.weather.api.datasource.location.error.AddFavouriteError
import github.makeitvsolo.kweather.weather.api.datasource.location.error.RemoveFavouriteError
import github.makeitvsolo.kweather.weather.api.datasource.location.operation.AddFavourite
import github.makeitvsolo.kweather.weather.api.datasource.location.operation.RemoveFavourite
import github.makeitvsolo.kweather.weather.infrastructure.datasource.location.sql.error.CreateTableError
import github.makeitvsolo.kweather.weather.infrastructure.datasource.location.sql.error.ExistsFavouriteError
import github.makeitvsolo.kweather.weather.infrastructure.datasource.location.sql.error.FindFavouriteCoordinatesError
import github.makeitvsolo.kweather.weather.infrastructure.datasource.location.sql.error.TruncateTableError
import github.makeitvsolo.kweather.weather.infrastructure.datasource.location.sql.data.Coordinates
import github.makeitvsolo.kweather.weather.infrastructure.datasource.location.sql.query.Defaults
import github.makeitvsolo.kweather.weather.infrastructure.datasource.location.sql.query.LocationQuery

import java.math.BigDecimal
import java.sql.SQLException
import javax.sql.DataSource

class SqlLocationRepository internal constructor(
    private val dataSource: DataSource
) : AddFavourite, RemoveFavourite {

    override fun addFavourite(
        accountId: String,
        latitude: BigDecimal,
        longitude: BigDecimal
    ): Result<Unit, AddFavouriteError> {
        try {
            dataSource.connection.use { connection ->
                val statement = connection.prepareStatement(LocationQuery.Insert.SQL)

                statement.use { sql ->
                    connection.autoCommit = false

                    sql.setString(LocationQuery.Insert.ACCOUNT_ID_PARAMETER, accountId)
                    sql.setBigDecimal(LocationQuery.Insert.LATITUDE_PARAMETER, latitude)
                    sql.setBigDecimal(LocationQuery.Insert.LONGITUDE_PARAMETER, longitude)

                    sql.execute()
                    connection.commit()
                }
            }

            return Result.ok(Unit)
        } catch (ex: SQLException) {
            if (ex.sqlState == POSTGRES_CONSTRAINT_ERROR_CODE) {
                return Result.error(AddFavouriteError.ConflictError("location already exists"))
            }

            if (ex.sqlState == POSTGRES_FOREIGN_KEY_ERROR_CODE) {
                return Result.error(AddFavouriteError.NotFoundError("account does not exists"))
            }

            return Result.error(AddFavouriteError.InternalError(ex))
        }
    }

    override fun removeFavourite(
        accountId: String,
        latitude: BigDecimal,
        longitude: BigDecimal
    ): Result<Unit, RemoveFavouriteError> {
        try {
            dataSource.connection.use { connection ->
                val statement = connection.prepareStatement(LocationQuery.Delete.SQL)

                statement.use { sql ->
                    connection.autoCommit = false

                    sql.setString(LocationQuery.Delete.ACCOUNT_ID_PARAMETER, accountId)
                    sql.setBigDecimal(LocationQuery.Delete.LATITUDE_PARAMETER, latitude)
                    sql.setBigDecimal(LocationQuery.Delete.LONGITUDE_PARAMETER, longitude)

                    sql.execute()
                    connection.commit()

                    val cursor = sql.resultSet
                    if (cursor.next()) {
                        return Result.ok(Unit)
                    }
                }
            }

            return Result.error(RemoveFavouriteError.NotFoundError("location does not exists"))
        } catch (ex: SQLException) {
            return Result.error(RemoveFavouriteError.InternalError(ex))
        }
    }

    fun existsFavourite(
        accountId: String,
        latitude: BigDecimal,
        longitude: BigDecimal
    ): Result<Boolean, ExistsFavouriteError> {
        try {
            dataSource.connection.use { connection ->
                val statement = connection.prepareStatement(LocationQuery.Exists.SQL)

                statement.use { sql ->
                    sql.setString(LocationQuery.Exists.ACCOUNT_ID_PARAMETER, accountId)
                    sql.setBigDecimal(LocationQuery.Exists.LATITUDE_PARAMETER, latitude)
                    sql.setBigDecimal(LocationQuery.Exists.LONGITUDE_PARAMETER, longitude)

                    sql.execute()

                    val cursor = sql.resultSet
                    if (cursor.next()) {
                        return Result.ok(cursor.getBoolean("exists"))
                    }
                }
            }

            return Result.ok(false)
        } catch (ex: SQLException) {
            return Result.error(ExistsFavouriteError(ex))
        }
    }

    fun findFavouriteCoordinates(accountId: String): Result<List<Coordinates>, FindFavouriteCoordinatesError> {
        try {
            dataSource.connection.use { connection ->
                val statement = connection.prepareStatement(LocationQuery.FindAll.SQL)

                statement.use { sql ->
                    sql.setString(LocationQuery.FindAll.ACCOUNT_ID_PARAMETER, accountId)

                    sql.execute()

                    val cursor = sql.resultSet
                    val coordinates: MutableList<Coordinates> = mutableListOf()
                    while (cursor.next()) {
                        coordinates.add(
                            Coordinates(
                                cursor.getBigDecimal("latitude"),
                                cursor.getBigDecimal("longitude")
                            )
                        )
                    }

                    return Result.ok(coordinates)
                }
            }
        } catch (ex: SQLException) {
            return Result.error(FindFavouriteCoordinatesError(ex))
        }
    }

    fun createTable(): Result<Unit, CreateTableError> {
        try {
            dataSource.connection.use { connection ->
                val statement = connection.prepareStatement(Defaults.CREATE_TABLE)

                statement.use { sql ->
                    connection.autoCommit = false

                    sql.execute()

                    connection.commit()
                }
            }

            return Result.ok(Unit)
        } catch (ex: SQLException) {
            return Result.error(CreateTableError(ex))
        }
    }

    fun truncateTable(): Result<Unit, TruncateTableError> {
        try {
            dataSource.connection.use { connection ->
                val statement = connection.prepareStatement(Defaults.TRUNCATE_TABLE)

                statement.use { sql ->
                    connection.autoCommit = false

                    sql.execute()

                    connection.commit()
                }
            }

            return Result.ok(Unit)
        } catch (ex: SQLException) {
            return Result.error(TruncateTableError(ex))
        }
    }

    companion object {

        private const val POSTGRES_CONSTRAINT_ERROR_CODE = "23505"
        private const val POSTGRES_FOREIGN_KEY_ERROR_CODE = "23503"
    }
}
