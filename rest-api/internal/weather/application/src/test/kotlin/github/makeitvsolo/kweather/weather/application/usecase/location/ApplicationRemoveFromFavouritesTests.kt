package github.makeitvsolo.kweather.weather.application.usecase.location

import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.weather.api.datasource.location.LocationRepository
import github.makeitvsolo.kweather.weather.api.datasource.location.operation.RemoveFavouriteError
import github.makeitvsolo.kweather.weather.api.service.location.usecase.RemoveFromFavouritesError
import github.makeitvsolo.kweather.weather.api.service.location.usecase.RemoveFromFavouritesPayload
import github.makeitvsolo.kweather.weather.application.ApplicationUnitTest

import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.kotlin.whenever

import java.math.BigDecimal

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ApplicationRemoveFromFavouritesTests : ApplicationUnitTest() {

    @Mock
    private lateinit var repository: LocationRepository

    @InjectMocks
    private lateinit var usecase: ApplicationRemoveFromFavourites

    @Test
    fun `removeFromFavourites removes location from favourites`() {
        val payload = RemoveFromFavouritesPayload("account id", BigDecimal.valueOf(0L), BigDecimal.valueOf(0L))
        val expected = Unit

        whenever(repository.removeFavourite(payload.accountId, payload.latitude, payload.longitude))
            .thenReturn(Result.ok(expected))

        val result = usecase.removeFromFavourites(payload)

        assertTrue(result.isOk)
        assertEquals(expected, result.unwrap())
    }

    @Test
    fun `removeFromFavourites returns not found error when account does not exists`() {
        val payload = RemoveFromFavouritesPayload("account id", BigDecimal.valueOf(0L), BigDecimal.valueOf(0L))
        val errorMessage = "account does not exists"
        val expected = RemoveFromFavouritesError.NotFoundError(errorMessage)

        whenever(repository.removeFavourite(payload.accountId, payload.latitude, payload.longitude))
            .thenReturn(Result.error(RemoveFavouriteError.NotFoundError(errorMessage)))

        val result = usecase.removeFromFavourites(payload)

        assertTrue(result.isError)
        assertEquals(expected, result.unwrapError())
    }

    @Test
    fun `removeFromFavourites returns not found error when location does not exists in favourites`() {
        val payload = RemoveFromFavouritesPayload("account id", BigDecimal.valueOf(0L), BigDecimal.valueOf(0L))
        val errorMessage = "location does not exists in favourites"
        val expected = RemoveFromFavouritesError.NotFoundError(errorMessage)

        whenever(repository.removeFavourite(payload.accountId, payload.latitude, payload.longitude))
            .thenReturn(Result.error(RemoveFavouriteError.NotFoundError(errorMessage)))

        val result = usecase.removeFromFavourites(payload)

        assertTrue(result.isError)
        assertEquals(expected, result.unwrapError())
    }

    @Test
    fun `removeFromFavourites returns internal error`() {
        val payload = RemoveFromFavouritesPayload("account id", BigDecimal.valueOf(0L), BigDecimal.valueOf(0L))
        val exception = Throwable("internal error")
        val expected = RemoveFromFavouritesError.InternalError(exception)

        whenever(repository.removeFavourite(payload.accountId, payload.latitude, payload.longitude))
            .thenReturn(Result.error(RemoveFavouriteError.InternalError(exception)))

        val result = usecase.removeFromFavourites(payload)

        assertTrue(result.isError)
        assertEquals(expected, result.unwrapError())
    }
}
