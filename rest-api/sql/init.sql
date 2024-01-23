CREATE TABLE IF NOT EXISTS users (
    id VARCHAR(60),
    name VARCHAR NOT NULL UNIQUE,
    password VARCHAR NOT NULL,

    PRIMARY KEY (id)
);

CREATE INDEX IF NOT EXISTS user_name_idx ON users(name);

CREATE TABLE IF NOT EXISTS users_locations (
    user_id VARCHAR(60) NOT NULL,
    latitude DECIMAL NOT NULL,
    longitude DECIMAL NOT NULL,

    FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE(user_id, latitude, longitude)
);

CREATE INDEX IF NOT EXISTS location_idx ON users_locations(user_id, latitude, longitude)
