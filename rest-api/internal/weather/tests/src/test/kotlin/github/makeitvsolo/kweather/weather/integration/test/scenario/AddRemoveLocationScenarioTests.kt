package github.makeitvsolo.kweather.weather.integration.test.scenario

import github.makeitvsolo.kweather.weather.api.service.location.dto.FavouriteLocationDto
import github.makeitvsolo.kweather.weather.api.service.location.error.SaveToFavouritesError
import github.makeitvsolo.kweather.weather.application.location.usecase.ApplicationRemoveFromFavourites
import github.makeitvsolo.kweather.weather.application.location.usecase.ApplicationSaveToFavourites
import github.makeitvsolo.kweather.weather.domain.account.Account
import github.makeitvsolo.kweather.weather.integration.test.WeatherIntegrationTest

import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.TestMethodOrder

import java.math.BigDecimal

import kotlin.test.Test
import kotlin.test.assertTrue

class AddRemoveLocationScenarioTests : WeatherIntegrationTest() {

    private val saveToFavouritesUsecase = ApplicationSaveToFavourites(
        locationRepository
    )

    private val removeFromFavouritesUsecase = ApplicationRemoveFromFavourites(
        locationRepository
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
    inner class WhenAccountExists {

        @Test
        fun `location can be added`() {
            saveToFavouritesUsecase.saveToFavourites(location)

            assertTrue(
                locationRepository.findFavourite(
                    location.accountId,
                    location.latitude,
                    location.longitude
                ).isOk
            )
        }

        @Nested
        @TestMethodOrder(MethodOrderer.OrderAnnotation::class)
        inner class WhenLocationInFavourites {

            @Test
            @Order(1)
            fun `location cannot be added again`() {
                val result = saveToFavouritesUsecase.saveToFavourites(location)

                assertTrue(result.isError)
                assertTrue(result.unwrapError() is SaveToFavouritesError.ConflictError)
            }

            @Test
            @Order(2)
            fun `location can be removed`() {
                removeFromFavouritesUsecase.removeFromFavourites(location)

                assertTrue(
                    locationRepository.findFavourite(
                        location.accountId,
                        location.latitude,
                        location.longitude
                    ).isError
                )
            }
        }
    }
}
