package github.makeitvsolo.kweather.weather.integration.test.scenario

import github.makeitvsolo.kweather.weather.api.service.location.dto.FavouriteLocationDto
import github.makeitvsolo.kweather.weather.api.service.weather.usecase.FetchForecastPayload
import github.makeitvsolo.kweather.weather.application.location.usecase.ApplicationSaveToFavourites
import github.makeitvsolo.kweather.weather.application.weather.usecase.ApplicationFetchForecast
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
class FetchForecastScenarioTests : WeatherIntegrationTest() {

    private val fetchForecastUsecase = ApplicationFetchForecast(
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
        mongoForecastRepository.createCollection().unwrap()
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
    @TestMethodOrder(MethodOrderer.OrderAnnotation::class)
    inner class WhenLocationInFavourite {

        @Test
        @Order(1)
        fun `by default cache is empty`() {
            assertTrue(
                mongoForecastRepository.findForecastByCoordinates(
                    location.latitude,
                    location.longitude
                ).isError
            )
        }

        @Test
        @Order(2)
        fun `forecast can be fetched`() {
            val result = fetchForecastUsecase.fetchForecast(
                FetchForecastPayload(location.accountId, location.latitude, location.longitude)
            )

            val fetched = result.unwrap()

            assertTrue(result.isOk)
            assertTrue(
                fetched.location.latitude == location.latitude && fetched.location.longitude == location.longitude
            )
        }

        @Test
        @Order(3)
        fun `after fetching cache is not empty`() {
            assertTrue(
                mongoForecastRepository.findForecastByCoordinates(
                    location.latitude,
                    location.longitude
                ).isOk
            )
        }

        @Nested
        inner class WhenCacheIsNotEmpty {

            @Test
            fun `cache can be cleaned`() {
                val result = weatherRepository.cleanCache()

                assertTrue(result.isOk)
                assertTrue(
                    mongoForecastRepository.findForecastByCoordinates(
                        location.latitude,
                        location.longitude
                    ).isError
                )
            }
        }
    }
}
