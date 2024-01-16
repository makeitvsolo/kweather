package github.makeitvsolo.kweather.weather.application.usecase.location

import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.weather.api.datasource.location.LocationRepository
import github.makeitvsolo.kweather.weather.api.datasource.location.operation.SearchLocationError
import github.makeitvsolo.kweather.weather.api.service.location.dto.LocationDto
import github.makeitvsolo.kweather.weather.api.service.location.usecase.SearchForLocationError
import github.makeitvsolo.kweather.weather.api.service.location.usecase.SearchForLocationPayload
import github.makeitvsolo.kweather.weather.application.ApplicationUnitTest
import github.makeitvsolo.kweather.weather.domain.location.Location

import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.kotlin.whenever

import java.math.BigDecimal

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ApplicationSearchForLocationTests : ApplicationUnitTest() {

    private val existingLocation = Location.from(
        "name",
        "region",
        "country",
        BigDecimal.valueOf(0L),
        BigDecimal.valueOf(0L),
        isFavourite = false
    )

    private val searchedLocations = listOf(existingLocation)

    @Mock
    private lateinit var repository: LocationRepository

    @InjectMocks
    private lateinit var usecase: ApplicationSearchForLocation

    @Test
    fun `search returns searched locations`() {
        val payload = SearchForLocationPayload("account id", "name")
        val location = LocationDto(
            "name",
            "region",
            "country",
            BigDecimal.valueOf(0L),
            BigDecimal.valueOf(0L),
            isFavourite = false
        )
        val expected = listOf(location)

        whenever(repository.searchByName(payload.accountId, payload.locationName))
            .thenReturn(Result.ok(searchedLocations))

        val result = usecase.search(payload)

        assertTrue(result.isOk)
        assertEquals(expected, result.unwrap())
    }

    @Test
    fun `search returns not found error when account does not exists`() {
        val payload = SearchForLocationPayload("account id", "name")
        val errorMessage = "account does not exists"
        val expected = SearchForLocationError.NotFoundError(errorMessage)

        whenever(repository.searchByName(payload.accountId, payload.locationName))
            .thenReturn(Result.error(SearchLocationError.NotFoundError(errorMessage)))

        val result = usecase.search(payload)

        assertTrue(result.isError)
        assertEquals(expected, result.unwrapError())
    }

    @Test
    fun `search returns internal error`() {
        val payload = SearchForLocationPayload("account id", "name")
        val exception = Throwable("internal error")
        val expected = SearchForLocationError.InternalError(exception)

        whenever(repository.searchByName(payload.accountId, payload.locationName))
            .thenReturn(Result.error(SearchLocationError.InternalError(exception)))

        val result = usecase.search(payload)

        assertTrue(result.isError)
        assertEquals(expected, result.unwrapError())
    }
}
