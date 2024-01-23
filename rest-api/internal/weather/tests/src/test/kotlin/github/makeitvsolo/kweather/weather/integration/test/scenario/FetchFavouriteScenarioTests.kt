package github.makeitvsolo.kweather.weather.integration.test.scenario

import github.makeitvsolo.kweather.weather.api.service.location.dto.AccountDto
import github.makeitvsolo.kweather.weather.api.service.location.dto.FavouriteLocationDto
import github.makeitvsolo.kweather.weather.application.location.usecase.ApplicationFetchFavourite
import github.makeitvsolo.kweather.weather.application.location.usecase.ApplicationSaveToFavourites
import github.makeitvsolo.kweather.weather.domain.account.Account
import github.makeitvsolo.kweather.weather.integration.test.WeatherIntegrationTest

import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.TestMethodOrder

import java.math.BigDecimal

import kotlin.test.Test
import kotlin.test.assertTrue

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class FetchFavouriteScenarioTests : WeatherIntegrationTest() {

    private val fetchFavouriteUsecase = ApplicationFetchFavourite(
        locationRepository
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

    @Test
    @Order(3)
    fun `favourite location can be fetched`() {
        val result = fetchFavouriteUsecase.fetchFavourite(location)

        val fetched = result.unwrap()

        assertTrue(result.isOk)
        assertTrue(fetched.latitude == location.latitude && fetched.longitude == location.longitude)
    }

    @Test
    @Order(4)
    fun `all favourites locations can be fetched`() {
        val result = fetchFavouriteUsecase.fetchAllFavourite(AccountDto(location.accountId))

        val fetched = result.unwrap()

        assertTrue(result.isOk)
        assertTrue(fetched.size == 1)
        assertTrue(fetched.any { it.latitude == location.latitude && it.longitude == location.longitude })
    }

    companion object {

        const val LATITUDE = 51.52
        const val LONGITUDE = -0.11
    }
}
