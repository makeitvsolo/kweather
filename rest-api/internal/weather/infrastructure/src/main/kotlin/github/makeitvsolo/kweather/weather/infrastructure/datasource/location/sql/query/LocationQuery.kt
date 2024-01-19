package github.makeitvsolo.kweather.weather.infrastructure.datasource.location.sql.query

internal object LocationQuery {

    object Insert {

        const val ACCOUNT_ID_PARAMETER = 1
        const val LATITUDE_PARAMETER = 2
        const val LONGITUDE_PARAMETER = 3
        const val SQL = """
            INSERT INTO users_locations (user_id, latitude, longitude)
            VALUES (?, ?, ?)
        """
    }

    object Delete {

        const val ACCOUNT_ID_PARAMETER = 1
        const val LATITUDE_PARAMETER = 2
        const val LONGITUDE_PARAMETER = 3
        const val SQL = """
            DELETE FROM users_locations
            WHERE user_id = ? AND latitude = ? AND longitude = ?
            RETURNING user_id, latitude, longitude
        """
    }

    object Exists {

        const val ACCOUNT_ID_PARAMETER = 1
        const val LATITUDE_PARAMETER = 2
        const val LONGITUDE_PARAMETER = 3
        const val SQL = """
            SELECT EXISTS(
                SELECT user_id, latitude, longitude
                FROM users_locations
                WHERE user_id = ? AND latitude = ? AND longitude = ?
            ) as exists
            FROM users_locations
        """
    }

    object FindAll {

        const val ACCOUNT_ID_PARAMETER = 1
        const val SQL = """
            SELECT latitude, longitude
            FROM users_locations
            WHERE user_id = ?
        """
    }
}