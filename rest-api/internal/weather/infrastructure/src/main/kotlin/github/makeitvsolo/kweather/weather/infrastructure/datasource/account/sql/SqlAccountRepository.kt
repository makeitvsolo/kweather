package github.makeitvsolo.kweather.weather.infrastructure.datasource.account.sql

import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.weather.domain.account.Account
import github.makeitvsolo.kweather.weather.infrastructure.datasource.account.sql.error.CreateTableError
import github.makeitvsolo.kweather.weather.infrastructure.datasource.account.sql.error.FindAccountError
import github.makeitvsolo.kweather.weather.infrastructure.datasource.account.sql.error.SaveAccountError
import github.makeitvsolo.kweather.weather.infrastructure.datasource.account.sql.error.TruncateTableError
import github.makeitvsolo.kweather.weather.infrastructure.datasource.account.sql.data.AccountParameters
import github.makeitvsolo.kweather.weather.infrastructure.datasource.account.sql.query.AccountQuery
import github.makeitvsolo.kweather.weather.infrastructure.datasource.account.sql.query.Defaults

import java.sql.SQLException
import javax.sql.DataSource

class SqlAccountRepository internal constructor(
    private val dataSource: DataSource
) {

    fun save(account: Account): Result<Unit, SaveAccountError> {
        try {
            dataSource.connection.use { connection ->
                val statement = connection.prepareStatement(AccountQuery.Insert.SQL)

                statement.use { sql ->
                    val parameters = account.into(AccountParameters.FromAccount)
                    connection.autoCommit = false

                    sql.setString(AccountQuery.Insert.ID_PARAMETER, parameters.id)
                    sql.setString(AccountQuery.Insert.NAME_PARAMETER, parameters.name)

                    sql.execute()
                    connection.commit()
                }
            }

            return Result.ok(Unit)
        } catch (ex: SQLException) {
            if (ex.sqlState == POSTGRES_CONSTRAINT_ERROR_CODE) {
                return Result.error(SaveAccountError.ConflictError("account already exists"))
            }

            return Result.error(SaveAccountError.InternalError(ex))
        }
    }

    fun findByName(name: String): Result<Account, FindAccountError> {
        try {
            dataSource.connection.use { connection ->
                val statement = connection.prepareStatement(AccountQuery.FetchByName.SQL)

                statement.use { sql ->
                    sql.setString(AccountQuery.FetchByName.NAME_PARAMETER, name)

                    sql.execute()

                    val cursor = sql.resultSet
                    if (cursor.next()) {
                        return Result.ok(
                            Account.from(
                                cursor.getString("id"),
                                cursor.getString("name")
                            )
                        )
                    }
                }
            }

            return Result.error(FindAccountError.NotFoundError("account does not exists"))
        } catch (ex: SQLException) {
            return Result.error(FindAccountError.InternalError(ex))
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
    }
}
