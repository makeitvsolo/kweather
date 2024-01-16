package github.makeitvsolo.kweather.weather.application.usecase.location

import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.weather.api.datasource.location.LocationRepository
import github.makeitvsolo.kweather.weather.api.datasource.location.operation.AddFavouriteError
import github.makeitvsolo.kweather.weather.api.service.location.usecase.SaveToFavouritesError
import github.makeitvsolo.kweather.weather.api.service.location.usecase.SaveToFavouritesPayload
import github.makeitvsolo.kweather.weather.application.ApplicationUnitTest

import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.kotlin.whenever

import java.math.BigDecimal

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ApplicationSaveToFavouritesTests : ApplicationUnitTest() {

    @Mock
    private lateinit var repository: LocationRepository

    @InjectMocks
    private lateinit var usecase: ApplicationSaveToFavourites

    @Test
    fun `saveToFavourites adds location to favourites`() {
        val payload = SaveToFavouritesPayload("account id", BigDecimal.valueOf(0L), BigDecimal.valueOf(0L))
        val expected = Unit

        whenever(repository.addFavourite(payload.accountId, payload.latitude, payload.longitude))
            .thenReturn(Result.ok(expected))

        val result = usecase.saveToFavourites(payload)

        assertTrue(result.isOk)
        assertEquals(expected, result.unwrap())
    }

    @Test
    fun `saveToFavourites returns error when location already in favourites`() {
        val payload = SaveToFavouritesPayload("account id", BigDecimal.valueOf(0L), BigDecimal.valueOf(0L))
        val errorMessage = "already in favourites"
        val expected = SaveToFavouritesError.ConflictError(errorMessage)

        whenever(repository.addFavourite(payload.accountId, payload.latitude, payload.longitude))
            .thenReturn(Result.error(AddFavouriteError.ConflictError(errorMessage)))

        val result = usecase.saveToFavourites(payload)

        assertTrue(result.isError)
        assertEquals(expected, result.unwrapError())
    }

    @Test
    fun `saveToFavourites returns error when account does not exists`() {
        val payload = SaveToFavouritesPayload("account id", BigDecimal.valueOf(0L), BigDecimal.valueOf(0L))
        val errorMessage = "account does not exists"
        val expected = SaveToFavouritesError.NotFoundError(errorMessage)

        whenever(repository.addFavourite(payload.accountId, payload.latitude, payload.longitude))
            .thenReturn(Result.error(AddFavouriteError.NotFoundError(errorMessage)))

        val result = usecase.saveToFavourites(payload)

        assertTrue(result.isError)
        assertEquals(expected, result.unwrapError())
    }

    @Test
    fun `saveToFavourites returns internal error`() {
        val payload = SaveToFavouritesPayload("account id", BigDecimal.valueOf(0L), BigDecimal.valueOf(0L))
        val exception = Throwable("internal error")
        val expected = SaveToFavouritesError.InternalError(exception)

        whenever(repository.addFavourite(payload.accountId, payload.latitude, payload.longitude))
            .thenReturn(Result.error(AddFavouriteError.InternalError(exception)))

        val result = usecase.saveToFavourites(payload)

        assertTrue(result.isError)
        assertEquals(expected, result.unwrapError())
    }
}
