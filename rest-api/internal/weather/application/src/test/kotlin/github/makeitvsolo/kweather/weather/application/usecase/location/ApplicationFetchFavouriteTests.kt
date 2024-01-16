package github.makeitvsolo.kweather.weather.application.usecase.location

import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.weather.api.datasource.location.LocationRepository
import github.makeitvsolo.kweather.weather.api.datasource.location.operation.FindFavouriteError
import github.makeitvsolo.kweather.weather.api.service.location.dto.LocationDto
import github.makeitvsolo.kweather.weather.api.service.location.usecase.FetchAllFavouritePayload
import github.makeitvsolo.kweather.weather.api.service.location.usecase.FetchFavouriteError
import github.makeitvsolo.kweather.weather.api.service.location.usecase.FetchFavouritePayload
import github.makeitvsolo.kweather.weather.application.ApplicationUnitTest
import github.makeitvsolo.kweather.weather.domain.location.Location

import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.kotlin.whenever

import java.math.BigDecimal

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ApplicationFetchFavouriteTests : ApplicationUnitTest() {

    private val existingLocation = Location.from(
        "name",
        "region",
        "country",
        BigDecimal.valueOf(0L),
        BigDecimal.valueOf(0L),
        isFavourite = true
    )

    private val favouriteLocations = listOf(existingLocation)

    @Mock
    private lateinit var repository: LocationRepository

    @InjectMocks
    private lateinit var usecase: ApplicationFetchFavourite

    @Test
    fun `fetchFavourite returns favourite location`() {
        val payload = FetchFavouritePayload(
            "account id",
            BigDecimal.valueOf(0L),
            BigDecimal.valueOf(0L)
        )
        val expected = LocationDto(
            "name",
            "region",
            "country",
            BigDecimal.valueOf(0L),
            BigDecimal.valueOf(0L),
            isFavourite = true
        )

        whenever(repository.findFavourite(payload.accountId, payload.latitude, payload.longitude))
            .thenReturn(Result.ok(existingLocation))

        val result = usecase.fetchFavourite(payload)

        assertTrue(result.isOk)
        assertEquals(expected, result.unwrap())
    }

    @Test
    fun `fetchFavourite returns not found error when account does not exists`() {
        val payload = FetchFavouritePayload(
            "account id",
            BigDecimal.valueOf(0L),
            BigDecimal.valueOf(0L)
        )
        val errorMessage = "account does not exists"
        val expected = FetchFavouriteError.NotFoundError(errorMessage)

        whenever(repository.findFavourite(payload.accountId, payload.latitude, payload.longitude))
            .thenReturn(Result.error(FindFavouriteError.NotFoundError(errorMessage)))

        val result = usecase.fetchFavourite(payload)

        assertTrue(result.isError)
        assertEquals(expected, result.unwrapError())
    }

    @Test
    fun `fetchFavourite returns not found error when location does not exists in favourites`() {
        val payload = FetchFavouritePayload(
            "account id",
            BigDecimal.valueOf(0L),
            BigDecimal.valueOf(0L)
        )
        val errorMessage = "location does not exists in favourites"
        val expected = FetchFavouriteError.NotFoundError(errorMessage)

        whenever(repository.findFavourite(payload.accountId, payload.latitude, payload.longitude))
            .thenReturn(Result.error(FindFavouriteError.NotFoundError(errorMessage)))

        val result = usecase.fetchFavourite(payload)

        assertTrue(result.isError)
        assertEquals(expected, result.unwrapError())
    }

    @Test
    fun `fetchFavourite returns internal error`() {
        val payload = FetchFavouritePayload(
            "account id",
            BigDecimal.valueOf(0L),
            BigDecimal.valueOf(0L)
        )
        val exception = Throwable("internal error")
        val expected = FetchFavouriteError.InternalError(exception)

        whenever(repository.findFavourite(payload.accountId, payload.latitude, payload.longitude))
            .thenReturn(Result.error(FindFavouriteError.InternalError(exception)))

        val result = usecase.fetchFavourite(payload)

        assertTrue(result.isError)
        assertEquals(expected, result.unwrapError())
    }

    @Test
    fun `fetchAllFavourite returns favourite locations`() {
        val payload = FetchAllFavouritePayload("account id")
        val location = LocationDto(
            "name",
            "region",
            "country",
            BigDecimal.valueOf(0L),
            BigDecimal.valueOf(0L),
            isFavourite = true
        )
        val expected = listOf(location)

        whenever(repository.findAllFavourite(payload.accountId))
            .thenReturn(Result.ok(favouriteLocations))

        val result = usecase.fetchAllFavourite(payload)

        assertTrue(result.isOk)
        assertEquals(expected, result.unwrap())
    }

    @Test
    fun `fetchAllFavourite returns not found error when account does not exists`() {
        val payload = FetchAllFavouritePayload("account id")
        val errorMessage = "account does not exists"
        val expected = FetchFavouriteError.NotFoundError(errorMessage)

        whenever(repository.findAllFavourite(payload.accountId))
            .thenReturn(Result.error(FindFavouriteError.NotFoundError(errorMessage)))

        val result = usecase.fetchAllFavourite(payload)

        assertTrue(result.isError)
        assertEquals(expected, result.unwrapError())
    }

    @Test
    fun `fetchAllFavourite returns internal error`() {
        val payload = FetchAllFavouritePayload("account id")
        val exception = Throwable("internal error")
        val expected = FetchFavouriteError.InternalError(exception)

        whenever(repository.findAllFavourite(payload.accountId))
            .thenReturn(Result.error(FindFavouriteError.InternalError(exception)))

        val result = usecase.fetchAllFavourite(payload)

        assertTrue(result.isError)
        assertEquals(expected, result.unwrapError())
    }
}
