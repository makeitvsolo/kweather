package github.makeitvsolo.kweather.weather.infrastructure.datasource.location.query

internal object Defaults {

    const val CREATE_TABLE = """
        CREATE TABLE IF NOT EXISTS users_locations (
	        user_id VARCHAR(60) NOT NULL,
  	        latitude DECIMAL NOT NULL,
  	        longitude DECIMAL NOT NULL,
  	
  	        FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE,
  	        UNIQUE(user_id, latitude, longitude)
        );
    """
    const val DROP_TABLE = """
        TRUNCATE users_locations;
    """
}
