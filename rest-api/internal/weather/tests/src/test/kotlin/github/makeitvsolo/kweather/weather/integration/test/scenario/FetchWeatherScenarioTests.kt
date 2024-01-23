package github.makeitvsolo.kweather.weather.integration.test.scenario

import github.makeitvsolo.kweather.weather.api.service.location.dto.FavouriteLocationDto
import github.makeitvsolo.kweather.weather.api.service.weather.usecase.FetchWeatherPayload
import github.makeitvsolo.kweather.weather.application.location.usecase.ApplicationSaveToFavourites
import github.makeitvsolo.kweather.weather.application.weather.usecase.ApplicationFetchWeather
import github.makeitvsolo.kweather.weather.domain.account.Account
import github.makeitvsolo.kweather.weather.integration.test.WeatherIntegrationTest

import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.TestMethodOrder

import java.math.BigDecimal

import kotlin.test.Test
import kotlin.test.assertTrue

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class FetchWeatherScenarioTests : WeatherIntegrationTest() {

    private val fetchWeatherUsecase = ApplicationFetchWeather(
        locationRepository, weatherRepository
    )

    private val saveToFavouritesUsecase = ApplicationSaveToFavourites(
        locationRepository
    )

    private val location = FavouriteLocationDto(
        "id", BigDecimal.valueOf(LATITUDE), BigDecimal.valueOf(LONGITUDE)
    )

    @Test
    @Order(1)
    fun `at start account exists`() {
        val account = Account.from(location.accountId, "name")

        sqlAccountRepository.createTable().unwrap()
        sqlLocationRepository.createTable().unwrap()
        sqlAccountRepository.save(account)

        assertTrue(sqlAccountRepository.findById(location.accountId).isOk)
    }

    @Test
    @Order(2)
    fun `and location in favourites`() {
        saveToFavouritesUsecase.saveToFavourites(location)

        assertTrue(
            locationRepository.findFavourite(
                location.accountId,
                location.latitude,
                location.longitude
            ).isOk
        )
    }

    companion object {

        const val LATITUDE = 51.52
        const val LONGITUDE = -0.11
    }

    @Nested
    inner class WhenLocationInFavourites {

        @Test
        fun `weather can be fetched`() {
            val result = fetchWeatherUsecase.fetchWeather(
                FetchWeatherPayload(location.accountId, location.latitude, location.longitude)
            )

            val fetched = result.unwrap()

            assertTrue(result.isOk)
            assertTrue(
                fetched.location.latitude == location.latitude && fetched.location.longitude == location.longitude
            )
        }
    }
}
