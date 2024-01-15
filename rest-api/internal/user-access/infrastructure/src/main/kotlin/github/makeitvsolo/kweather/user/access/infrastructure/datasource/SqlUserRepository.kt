package github.makeitvsolo.kweather.user.access.infrastructure.datasource

import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.user.access.api.datasource.UserRepository
import github.makeitvsolo.kweather.user.access.api.datasource.operation.FindUserError
import github.makeitvsolo.kweather.user.access.api.datasource.operation.SaveUserError
import github.makeitvsolo.kweather.user.access.domain.User
import github.makeitvsolo.kweather.user.access.infrastructure.datasource.parameter.UserParameters
import github.makeitvsolo.kweather.user.access.infrastructure.datasource.query.Defaults
import github.makeitvsolo.kweather.user.access.infrastructure.datasource.query.UserQuery

import java.sql.SQLException
import java.sql.SQLIntegrityConstraintViolationException
import javax.sql.DataSource

data class CreateTableError internal constructor(private val throwable: Throwable)

data class DropTableError internal constructor(private val throwable: Throwable)

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
        } catch (ex: SQLIntegrityConstraintViolationException) {
            return Result.error(SaveUserError.ConflictError("user already exists"))
        } catch (ex: SQLException) {
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

    fun dropTable(): Result<Unit, DropTableError> {
        try {
            dataSource.connection.use { connection ->
                val statement = connection.prepareStatement(Defaults.DROP_TABLE)

                statement.use { sql ->
                    connection.autoCommit = false

                    sql.execute()

                    connection.commit()
                }
            }

            return Result.ok(Unit)
        } catch (ex: SQLException) {
            return Result.error(DropTableError(ex))
        }
    }
}
