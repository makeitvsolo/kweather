package github.makeitvsolo.kweather.weather.infrastructure.datasource.account.sql.query

internal object Defaults {

    const val CREATE_TABLE = """
        CREATE TABLE IF NOT EXISTS users (
            id VARCHAR(60),
            name VARCHAR NOT NULL UNIQUE,
            PRIMARY KEY (id)
        );
    """
    const val TRUNCATE_TABLE = """
        TRUNCATE users;
    """
}
