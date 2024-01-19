package github.makeitvsolo.kweather.user.access.infrastructure.datasource.sql

import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.user.access.api.datasource.user.UserRepository
import github.makeitvsolo.kweather.user.access.api.datasource.user.error.FindUserError
import github.makeitvsolo.kweather.user.access.api.datasource.user.error.SaveUserError
import github.makeitvsolo.kweather.user.access.domain.User
import github.makeitvsolo.kweather.user.access.infrastructure.datasource.sql.data.UserParameters
import github.makeitvsolo.kweather.user.access.infrastructure.datasource.sql.error.CreateTableError
import github.makeitvsolo.kweather.user.access.infrastructure.datasource.sql.error.TruncateTableError
import github.makeitvsolo.kweather.user.access.infrastructure.datasource.sql.query.Defaults
import github.makeitvsolo.kweather.user.access.infrastructure.datasource.sql.query.UserQuery

import java.sql.SQLException
import javax.sql.DataSource

class SqlUserRepository internal constructor(
    private val dataSource: DataSource
) : UserRepository {

    override fun save(user: User): Result<Unit, SaveUserError> {
        try {
            dataSource.connection.use { connection ->
                val statement = connection.prepareStatement(UserQuery.Insert.SQL)

                statement.use { sql ->
                    val parameters = user.into(UserParameters.FromUser)
                    connection.autoCommit = false

                    sql.setString(UserQuery.Insert.ID_PARAMETER, parameters.id)
                    sql.setString(UserQuery.Insert.NAME_PARAMETER, parameters.name)
                    sql.setString(UserQuery.Insert.PASSWORD_PARAMETER, parameters.password)

                    sql.execute()
                    connection.commit()
                }
            }

            return Result.ok(Unit)
        } catch (ex: SQLException) {
            if (ex.sqlState == POSTGRES_CONSTRAINT_ERROR_CODE) {
                return Result.error(SaveUserError.ConflictError("user already exists"))
            }

            return Result.error(SaveUserError.InternalError(ex))
        }
    }

    override fun findByName(name: String): Result<User, FindUserError> {
        try {
            dataSource.connection.use { connection ->
                val statement = connection.prepareStatement(UserQuery.FetchByName.SQL)

                statement.use { sql ->
                    sql.setString(UserQuery.FetchByName.NAME_PARAMETER, name)

                    sql.execute()

                    val cursor = sql.resultSet
                    if (cursor.next()) {
                        return Result.ok(
                            User.from(
                                cursor.getString("id"),
                                cursor.getString("name"),
                                cursor.getString("password")
                            )
                        )
                    }
                }
            }

            return Result.error(FindUserError.NotFoundError("user does not exists"))
        } catch (ex: SQLException) {
            return Result.error(FindUserError.InternalError(ex))
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
