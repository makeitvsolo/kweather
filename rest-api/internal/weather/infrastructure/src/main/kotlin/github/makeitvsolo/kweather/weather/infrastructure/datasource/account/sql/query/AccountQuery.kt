package github.makeitvsolo.kweather.weather.infrastructure.datasource.account.sql.query

import github.makeitvsolo.kweather.weather.domain.account.Account

import java.sql.ResultSet

internal object AccountQuery {

    object Insert {

        const val ID_PARAMETER = 1
        const val NAME_PARAMETER = 2
        const val SQL = """
            INSERT INTO users (id, name)
            VALUES (?, ?)
        """
    }

    object FetchById {

        const val ID_PARAMETER = 1
        const val SQL = """
            SELECT id, name
            FROM users
            WHERE id = ?
        """
    }
}

internal fun ResultSet.intoAccount(): Account =
    Account.from(
        getString("id"),
        getString("name")
    )
