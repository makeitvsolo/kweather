package github.makeitvsolo.kweather.boot.api.controller.weather

import github.makeitvsolo.kweather.boot.api.controller.weather.request.CoordinatesRequest
import github.makeitvsolo.kweather.boot.api.controller.weather.response.AddToFavouritesResponse
import github.makeitvsolo.kweather.boot.api.controller.weather.response.FetchForecastResponse
import github.makeitvsolo.kweather.boot.api.controller.weather.response.FetchLocationsResponse
import github.makeitvsolo.kweather.boot.api.controller.weather.response.FetchWeatherResponse
import github.makeitvsolo.kweather.boot.api.controller.weather.response.RemoveFromFavouritesResponse
import github.makeitvsolo.kweather.boot.api.controller.weather.response.SearchLocationResponse
import github.makeitvsolo.kweather.boot.configuration.spring.session.jwt.Authenticated
import github.makeitvsolo.kweather.user.access.api.service.user.dto.ActiveUserDto
import github.makeitvsolo.kweather.weather.api.service.location.LocationService
import github.makeitvsolo.kweather.weather.api.service.location.usecase.FetchAllFavouritePayload
import github.makeitvsolo.kweather.weather.api.service.location.usecase.RemoveFromFavouritesPayload
import github.makeitvsolo.kweather.weather.api.service.location.usecase.SaveToFavouritesPayload
import github.makeitvsolo.kweather.weather.api.service.location.usecase.SearchForLocationPayload
import github.makeitvsolo.kweather.weather.api.service.weather.WeatherService
import github.makeitvsolo.kweather.weather.api.service.weather.usecase.FetchForecastPayload
import github.makeitvsolo.kweather.weather.api.service.weather.usecase.FetchWeatherPayload

import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal

@RestController
@RequestMapping("api/v1/locations")
open class LocationController(
    private val locationService: LocationService,
    private val weatherService: WeatherService
) {

    @PostMapping(consumes = ["application/json"])
    open fun addToFavourites(
        @Authenticated user: ActiveUserDto,
        @Valid @RequestBody coordinates: CoordinatesRequest
    ): ResponseEntity<*> {
        log.info("trying to add ${coordinates.latitude},${coordinates.longitude} for user: ${user.name}...")
        val payload = SaveToFavouritesPayload(user.id, coordinates.latitude, coordinates.longitude)

        return locationService.saveToFavourites(payload).map {
            log.info("${coordinates.latitude},${coordinates.longitude} successfully added for ${user.name}")
            AddToFavouritesResponse.fromOk()
        }.unwrapOrElse { error ->
            log.info("add $coordinates for user: ${user.name} error: $error")
            AddToFavouritesResponse.fromError(error)
        }
    }

    @DeleteMapping(value = ["/{lat},{lon}"])
    open fun removeFromFavourites(
        @Authenticated user: ActiveUserDto,
        @PathVariable(name = "lat") latitude: BigDecimal,
        @PathVariable(name = "lon") longitude: BigDecimal
    ): ResponseEntity<*> {
        log.info("trying to remove $latitude,$longitude for user: ${user.name}...")
        val payload = RemoveFromFavouritesPayload(user.id, latitude, longitude)

        return locationService.removeFromFavourites(payload).map {
            log.info("$latitude,$longitude successfully removed for ${user.name}")
            RemoveFromFavouritesResponse.fromOk()
        }.unwrapOrElse { error ->
            log.info("remove $latitude,$longitude for user: ${user.name} error: $error")
            RemoveFromFavouritesResponse.fromError(error)
        }
    }

    @GetMapping
    open fun locations(
        @Authenticated user: ActiveUserDto,
        @RequestParam(name = "search", required = false) locationName: String?
    ): ResponseEntity<*> {
        locationName?.let {
            log.info("trying to search $locationName for ${user.name}...")
            val payload = SearchForLocationPayload(user.id, locationName)

            return locationService.search(payload).map { response ->
                log.info("send searched locations to ${user.name}")
                SearchLocationResponse.fromOk(response)
            }.unwrapOrElse { error ->
                log.info("search $locationName for ${user.name} error: $error")
                SearchLocationResponse.fromError(error)
            }
        }

        log.info("trying to fetch all favourites for ${user.name}...")
        val payload = FetchAllFavouritePayload(user.id)

        return locationService.fetchAllFavourite(payload).map { response ->
            log.info("send all favourites locations to ${user.name}")
            FetchLocationsResponse.fromOk(response)
        }.unwrapOrElse { error ->
            log.info("fetch all favourites for ${user.name} error: $error")
            FetchLocationsResponse.fromError(error)
        }
    }

    @GetMapping(value = ["/{lat},{lon}/weather"])
    open fun weather(
        @Authenticated user: ActiveUserDto,
        @PathVariable(name = "lat") latitude: BigDecimal,
        @PathVariable(name = "lon") longitude: BigDecimal
    ): ResponseEntity<*> {
        log.info("trying to fetch weather on $latitude,$longitude for ${user.name}...")
        val payload = FetchWeatherPayload(user.id, latitude, longitude)

        return weatherService.fetchWeather(payload).map { response ->
            log.info("send weather on $latitude,$longitude to ${user.name}")
            FetchWeatherResponse.fromOk(response)
        }.unwrapOrElse { error ->
            log.info("fetch weather on $latitude,$longitude for ${user.name} error: $error")
            FetchWeatherResponse.fromError(error)
        }
    }

    @GetMapping(value = ["/{lat},{lon}/forecast"])
    open fun forecast(
        @Authenticated user: ActiveUserDto,
        @PathVariable(name = "lat") latitude: BigDecimal,
        @PathVariable(name = "lon") longitude: BigDecimal
    ): ResponseEntity<*> {
        log.info("trying to fetch forecast on $latitude,$longitude for ${user.name}...")
        val payload = FetchForecastPayload(user.id, latitude, longitude)

        return weatherService.fetchForecast(payload).map { response ->
            log.info("send forecast on $latitude,$longitude to ${user.name}")
            FetchForecastResponse.fromOk(response)
        }.unwrapOrElse { error ->
            log.info("fetch forecast on $latitude,$longitude for ${user.name} error: $error")
            FetchForecastResponse.fromError(error)
        }
    }

    companion object {

        private val log = LoggerFactory.getLogger(LocationController::class.java)
    }
}
