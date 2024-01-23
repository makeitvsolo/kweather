package github.makeitvsolo.kweather.user.access.infrastructure.datasource.sql.query

import github.makeitvsolo.kweather.user.access.domain.User

import java.sql.ResultSet

internal object UserQuery {

    object Insert {

        const val ID_PARAMETER = 1
        const val NAME_PARAMETER = 2
        const val PASSWORD_PARAMETER = 3
        const val SQL = """
            INSERT INTO users (id, name, password)
            VALUES (?, ?, ?)
        """
    }

    object FetchByName {

        const val NAME_PARAMETER = 1
        const val SQL = """
            SELECT id, name, password
            FROM users
            WHERE name = ?
        """
    }
}

internal fun ResultSet.intoUser(): User =
    User.from(
        getString("id"),
        getString("name"),
        getString("password")
    )
