# kweather

Weather web application (Rewritten version of [rainy-today](https://github.com/makeitvsolo/rainy-today)).

# table of contents
1. [features](#feats)
2. [technologies / tools](#tools)
3. [api overview](#api)
4. [screenshots](#screenshots)

# features <a id="feats"></a>

### users:
- sign up
- sign in

### Locations:
- adding from favourites/removing from favourites
- searching for location
- fetching weather on location
- fetching forecast on location

# technologies / tools <a id="tools"></a>

### Backend:
- Kotlin (jdk 17)
- Spring boot
- PostgreSQL
- Mongo
- Fuel
- Jwt
- Junit5, Mockito, Testcontainers
- Gradle
### Frontend:
- Not implemented yet...
### CI/CD:
- Docker, docker-compose
- Github actions

# api overview <a id="api"></a>

## user-access

```shell
# sign-up
curl --verbose \
     --header 'Content-Type: application/json' \
     --request POST \
     --data '{"name": "yourname", "password": "passwd"}' \
     '/api/v1/user-access/sign-up'

# sign-in
curl --verbose \
     --header 'Content-Type: application/json' \
     --request POST \
     --data '{"name": "yourname", "password": "passwd"}' \
     '/api/v1/user-access/sign-in'

# refresh
curl --verbose \
     --header 'Content-Type: application/json' \
     --request POST \
     --data '{"token": "$TOKEN"}' \
     '/api/v1/user-access/refresh-token'
```

## locations

```shell
# add to favourites
curl --verbose \
     --header 'Content-Type: application/json' \
     --header 'Authorization: Bearer $TOKEN' \
     --request POST \
     --data '{"latitude": 51.52, "longitude": -0.11}' \
     '/api/v1/locations'

# remove from favourites
curl --verbose \
     --header 'Content-Type: application/json' \
     --header 'Authorization: Bearer $TOKEN' \
     --request DELETE \
     '/api/v1/locations/51.52,-0.11'

# fetch favourites
curl --verbose \
     --header 'Content-Type: application/json' \
     --header 'Authorization: Bearer $TOKEN' \
     --request GET \
     '/api/v1/locations'

# search by name
curl --verbose \
     --header 'Content-Type: application/json' \
     --header 'Authorization: Bearer $TOKEN' \
     --request GET \
     '/api/v1/locations?search=London'

# fetch weather
curl --verbose \
     --header 'Content-Type: application/json' \
     --header 'Authorization: Bearer $TOKEN' \
     --request GET \
     '/api/v1/locations/51.52,-0.11/weather'

# fetch forecast
curl --verbose \
     --header 'Content-Type: application/json' \
     --header 'Authorization: Bearer $TOKEN' \
     --request GET \
     '/api/v1/locations/51.52,-0.11/forecast'
```

# screenshots <a id="screenshots"></a>

Not implemented yet...
