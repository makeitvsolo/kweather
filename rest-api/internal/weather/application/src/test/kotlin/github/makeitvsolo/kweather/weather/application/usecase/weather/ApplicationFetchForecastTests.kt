package github.makeitvsolo.kweather.weather.application.usecase.weather

import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.weather.api.datasource.location.LocationRepository
import github.makeitvsolo.kweather.weather.api.datasource.location.operation.FindFavouriteError
import github.makeitvsolo.kweather.weather.api.datasource.weather.WeatherRepository
import github.makeitvsolo.kweather.weather.api.datasource.weather.operation.FindForecastError
import github.makeitvsolo.kweather.weather.api.service.weather.dto.DailyTemperatureDto
import github.makeitvsolo.kweather.weather.api.service.weather.dto.DailyWeatherDto
import github.makeitvsolo.kweather.weather.api.service.weather.dto.LocationDto
import github.makeitvsolo.kweather.weather.api.service.weather.usecase.FetchForecastError
import github.makeitvsolo.kweather.weather.api.service.weather.usecase.FetchForecastPayload
import github.makeitvsolo.kweather.weather.api.service.weather.usecase.FetchForecastResponse
import github.makeitvsolo.kweather.weather.application.ApplicationUnitTest
import github.makeitvsolo.kweather.weather.domain.location.Location
import github.makeitvsolo.kweather.weather.domain.weather.forecast.DailyWeather
import github.makeitvsolo.kweather.weather.domain.weather.forecast.value.DailyHumidity
import github.makeitvsolo.kweather.weather.domain.weather.forecast.value.DailyPrecipitation
import github.makeitvsolo.kweather.weather.domain.weather.forecast.value.DailyTemperature
import github.makeitvsolo.kweather.weather.domain.weather.forecast.value.DailyWind

import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.kotlin.whenever

import java.math.BigDecimal
import java.time.LocalDate

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ApplicationFetchForecastTests : ApplicationUnitTest() {

    private val existingLocation = Location.from(
        "name",
        "region",
        "country",
        BigDecimal.valueOf(0L),
        BigDecimal.valueOf(0L),
        isFavourite = true
    )

    private val existingDailyWeather = DailyWeather.from(
        WEATHER_CODE,
        "summary",
        LocalDate.MIN,
        DailyTemperature(TEMPERATURE, TEMPERATURE_MAX, TEMPERATURE_MIN),
        DailyWind(WIND),
        DailyHumidity(HUMIDITY),
        DailyPrecipitation(PRECIPITATION, RAIN_CHANCE, SNOW_CHANCE)
    )

    private val forecast = listOf(existingDailyWeather)

    @Mock
    private lateinit var locationRepository: LocationRepository

    @Mock
    private lateinit var weatherRepository: WeatherRepository

    @InjectMocks
    private lateinit var usecase: ApplicationFetchForecast

    @Test
    fun `fetchForecast returns forecast`() {
        val payload = FetchForecastPayload("account id", BigDecimal.valueOf(0L), BigDecimal.valueOf(0L))
        val location = LocationDto(
            "name",
            "region",
            "country",
            BigDecimal.valueOf(0L),
            BigDecimal.valueOf(0L)
        )
        val dailyWeather = DailyWeatherDto(
            WEATHER_CODE,
            "summary",
            LocalDate.MIN,
            DailyTemperatureDto(TEMPERATURE, TEMPERATURE_MAX, TEMPERATURE_MIN),
            WIND,
            HUMIDITY,
            PRECIPITATION,
            RAIN_CHANCE,
            SNOW_CHANCE
        )
        val expected = FetchForecastResponse(
            location,
            listOf(dailyWeather)
        )

        whenever(locationRepository.findFavourite(payload.accountId, payload.latitude, payload.longitude))
            .thenReturn(Result.ok(existingLocation))
        whenever(weatherRepository.findForecastByCoordinates(payload.latitude, payload.longitude))
            .thenReturn(Result.ok(forecast))

        val result = usecase.fetchForecast(payload)

        assertTrue(result.isOk)
        assertEquals(expected, result.unwrap())
    }

    @Test
    fun `fetchForecast returns not found error when account does not exists`() {
        val payload = FetchForecastPayload("account id", BigDecimal.valueOf(0L), BigDecimal.valueOf(0L))
        val errorMessage = "account does not exists"
        val expected = FetchForecastError.NotFoundError(errorMessage)

        whenever(locationRepository.findFavourite(payload.accountId, payload.latitude, payload.longitude))
            .thenReturn(Result.error(FindFavouriteError.NotFoundError(errorMessage)))

        val result = usecase.fetchForecast(payload)

        assertTrue(result.isError)
        assertEquals(expected, result.unwrapError())
    }

    @Test
    fun `fetchForecast returns not found error when favourite location does not exists`() {
        val payload = FetchForecastPayload("account id", BigDecimal.valueOf(0L), BigDecimal.valueOf(0L))
        val errorMessage = "location does not exists"
        val expected = FetchForecastError.NotFoundError(errorMessage)

        whenever(locationRepository.findFavourite(payload.accountId, payload.latitude, payload.longitude))
            .thenReturn(Result.error(FindFavouriteError.NotFoundError(errorMessage)))

        val result = usecase.fetchForecast(payload)

        assertTrue(result.isError)
        assertEquals(expected, result.unwrapError())
    }

    @Test
    fun `fetchForecast returns not found error when forecast not found`() {
        val payload = FetchForecastPayload("account id", BigDecimal.valueOf(0L), BigDecimal.valueOf(0L))
        val errorMessage = "forecast not found"
        val expected = FetchForecastError.NotFoundError(errorMessage)

        whenever(locationRepository.findFavourite(payload.accountId, payload.latitude, payload.longitude))
            .thenReturn(Result.ok(existingLocation))
        whenever(weatherRepository.findForecastByCoordinates(payload.latitude, payload.longitude))
            .thenReturn(Result.error(FindForecastError.NotFoundError(errorMessage)))

        val result = usecase.fetchForecast(payload)

        assertTrue(result.isError)
        assertEquals(expected, result.unwrapError())
    }

    @Test
    fun `fetchForecast returns internal error`() {
        val payload = FetchForecastPayload("account id", BigDecimal.valueOf(0L), BigDecimal.valueOf(0L))
        val exception = Throwable("internal error")
        val expected = FetchForecastError.InternalError(exception)

        whenever(locationRepository.findFavourite(payload.accountId, payload.latitude, payload.longitude))
            .thenReturn(Result.error(FindFavouriteError.InternalError(exception)))

        val result = usecase.fetchForecast(payload)

        assertTrue(result.isError)
        assertEquals(expected, result.unwrapError())
    }

    companion object {

        private const val HUMIDITY = 0
        private const val PRECIPITATION = 0.0
        private const val RAIN_CHANCE = 0
        private const val SNOW_CHANCE = 0
        private const val TEMPERATURE = 0.0
        private const val TEMPERATURE_MAX = 0.0
        private const val TEMPERATURE_MIN = 0.0
        private const val WEATHER_CODE = 0
        private const val WIND = 0.0
    }
}
