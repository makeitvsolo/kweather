package github.makeitvsolo.kweather.weather.integration.test.scenario

import github.makeitvsolo.kweather.weather.api.service.location.dto.FavouriteLocationDto
import github.makeitvsolo.kweather.weather.api.service.location.dto.SearchLocationDto
import github.makeitvsolo.kweather.weather.application.location.usecase.ApplicationSaveToFavourites
import github.makeitvsolo.kweather.weather.application.location.usecase.ApplicationSearchForLocation
import github.makeitvsolo.kweather.weather.domain.account.Account
import github.makeitvsolo.kweather.weather.integration.test.WeatherIntegrationTest

import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.TestMethodOrder

import java.math.BigDecimal

import kotlin.test.Test
import kotlin.test.assertTrue

class SearchLocationScenarioTests : WeatherIntegrationTest() {

    private val saveToFavouritesUsecase = ApplicationSaveToFavourites(
        locationRepository
    )

    private val searchLocationUsecase = ApplicationSearchForLocation(
        locationRepository
    )

    private val query = SearchLocationDto(
        "id", "London"
    )

    private val location = FavouriteLocationDto(
        "id", BigDecimal.valueOf(LATITUDE), BigDecimal.valueOf(LONGITUDE)
    )

    @Test
    fun `at start account exists`() {
        val account = Account.from(location.accountId, "name")

        sqlAccountRepository.createTable().unwrap()
        sqlLocationRepository.createTable().unwrap()
        sqlAccountRepository.save(account)

        assertTrue(sqlAccountRepository.findById(location.accountId).isOk)
    }

    companion object {

        const val LATITUDE = 51.52
        const val LONGITUDE = -0.11
    }

    @Nested
    inner class WhenLocationNotInFavourites {

        @Test
        fun `searched location not marked as favourite`() {
            val result = searchLocationUsecase.search(query)

            val locations = result.unwrap()

            assertTrue(result.isOk)
            assertTrue(locations.all { it.name == query.locationName && !it.isFavourite })
        }

        @Nested
        @TestMethodOrder(MethodOrderer.OrderAnnotation::class)
        inner class WhenLocationInFavourites {

            @Test
            @Order(1)
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

            @Test
            @Order(2)
            fun `searched location marked as favourite`() {
                val result = searchLocationUsecase.search(query)

                val locations = result.unwrap()

                assertTrue(result.isOk)
                assertTrue(locations.any { it.name == query.locationName && it.isFavourite })
            }
        }
    }
}
