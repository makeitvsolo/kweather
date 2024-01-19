package github.makeitvsolo.kweather.user.access.infrastructure.datasource.sql.query

internal object Defaults {

    const val CREATE_TABLE = """
        CREATE TABLE IF NOT EXISTS users (
            id VARCHAR(60),
            name VARCHAR NOT NULL UNIQUE,
            password VARCHAR NOT NULL,
            PRIMARY KEY (id)
        );
    """
    const val TRUNCATE_TABLE = """
        TRUNCATE users;
    """
}
