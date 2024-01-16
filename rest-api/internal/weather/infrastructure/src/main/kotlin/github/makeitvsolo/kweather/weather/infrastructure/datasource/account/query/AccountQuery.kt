package github.makeitvsolo.kweather.weather.infrastructure.datasource.account.query

internal object AccountQuery {

    object Insert {

        const val ID_PARAMETER = 1
        const val NAME_PARAMETER = 2
        const val SQL = """
            INSERT INTO users (id, name)
            VALUES (?, ?)
        """
    }

    object FetchByName {

        const val NAME_PARAMETER = 1
        const val SQL = """
            SELECT id, name
            FROM users
            WHERE name = ?
        """
    }
}
