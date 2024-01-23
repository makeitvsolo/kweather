package github.makeitvsolo.kweather.weather.application.weather.usecase

import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.weather.api.datasource.location.LocationRepository
import github.makeitvsolo.kweather.weather.api.datasource.location.error.FindFavouriteError
import github.makeitvsolo.kweather.weather.api.datasource.weather.WeatherRepository
import github.makeitvsolo.kweather.weather.api.datasource.weather.error.FindWeatherError
import github.makeitvsolo.kweather.weather.api.service.weather.dto.LocationDto
import github.makeitvsolo.kweather.weather.api.service.weather.dto.TemperatureDto
import github.makeitvsolo.kweather.weather.api.service.weather.dto.WeatherDto
import github.makeitvsolo.kweather.weather.api.service.weather.dto.WindDto
import github.makeitvsolo.kweather.weather.api.service.weather.error.FetchWeatherError
import github.makeitvsolo.kweather.weather.api.service.weather.usecase.FetchWeatherPayload
import github.makeitvsolo.kweather.weather.api.service.weather.usecase.FetchWeatherResponse
import github.makeitvsolo.kweather.weather.application.ApplicationUnitTest
import github.makeitvsolo.kweather.weather.domain.location.Location
import github.makeitvsolo.kweather.weather.domain.weather.current.Weather
import github.makeitvsolo.kweather.weather.domain.weather.current.value.Cloudiness
import github.makeitvsolo.kweather.weather.domain.weather.current.value.Humidity
import github.makeitvsolo.kweather.weather.domain.weather.current.value.Precipitation
import github.makeitvsolo.kweather.weather.domain.weather.current.value.Pressure
import github.makeitvsolo.kweather.weather.domain.weather.current.value.Temperature
import github.makeitvsolo.kweather.weather.domain.weather.current.value.Wind

import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.kotlin.whenever

import java.math.BigDecimal
import java.time.LocalDateTime

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ApplicationFetchWeatherTests : ApplicationUnitTest() {

    private val existingLocation = Location.from(
        "name",
        "region",
        "country",
        BigDecimal.valueOf(0L),
        BigDecimal.valueOf(0L),
        isFavourite = true
    )

    private val existingWeather = Weather.from(
        WEATHER_CODE,
        "summary",
        LocalDateTime.MIN,
        Temperature(CURRENT_TEMPERATURE, FEELS_LIKE_TEMPERATURE),
        Wind(WIND_SPEED, WIND_GUST, WIND_DEGREE, "direction"),
        Pressure(PRESSURE),
        Humidity(HUMIDITY),
        Cloudiness(CLOUDINESS),
        Precipitation(PRECIPITATION)
    )

    @Mock
    private lateinit var locationRepository: LocationRepository

    @Mock
    private lateinit var weatherRepository: WeatherRepository

    @InjectMocks
    private lateinit var usecase: ApplicationFetchWeather

    @Test
    fun `fetchWeather returns weather`() {
        val payload = FetchWeatherPayload("account id", BigDecimal.valueOf(0L), BigDecimal.valueOf(0L))
        val location = LocationDto(
            "name",
            "region",
            "country",
            BigDecimal.valueOf(0L),
            BigDecimal.valueOf(0L)
        )
        val weather = WeatherDto(
            WEATHER_CODE,
            "summary",
            LocalDateTime.MIN,
            TemperatureDto(CURRENT_TEMPERATURE, FEELS_LIKE_TEMPERATURE),
            WindDto(WIND_SPEED, WIND_GUST, WIND_DEGREE, "direction"),
            PRESSURE,
            HUMIDITY,
            CLOUDINESS,
            PRECIPITATION,
        )
        val expected = FetchWeatherResponse(
            location, weather
        )

        whenever(locationRepository.findFavourite(payload.accountId, payload.latitude, payload.longitude))
            .thenReturn(Result.ok(existingLocation))
        whenever(weatherRepository.findByCoordinates(payload.latitude, payload.longitude))
            .thenReturn(Result.ok(existingWeather))

        val result = usecase.fetchWeather(payload)

        assertTrue(result.isOk)
        assertEquals(expected, result.unwrap())
    }

    @Test
    fun `fetchWeather returns not found error when account does not exists`() {
        val payload = FetchWeatherPayload("account id", BigDecimal.valueOf(0L), BigDecimal.valueOf(0L))
        val errorMessage = "account does not exists"
        val expected = FetchWeatherError.NotFoundError(errorMessage)

        whenever(locationRepository.findFavourite(payload.accountId, payload.latitude, payload.longitude))
            .thenReturn(Result.error(FindFavouriteError.NotFoundError(errorMessage)))

        val result = usecase.fetchWeather(payload)

        assertTrue(result.isError)
        assertEquals(expected, result.unwrapError())
    }

    @Test
    fun `fetchWeather returns not found error when favourite location does not exists`() {
        val payload = FetchWeatherPayload("account id", BigDecimal.valueOf(0L), BigDecimal.valueOf(0L))
        val errorMessage = "location does not exists"
        val expected = FetchWeatherError.NotFoundError(errorMessage)

        whenever(locationRepository.findFavourite(payload.accountId, payload.latitude, payload.longitude))
            .thenReturn(Result.error(FindFavouriteError.NotFoundError(errorMessage)))

        val result = usecase.fetchWeather(payload)

        assertTrue(result.isError)
        assertEquals(expected, result.unwrapError())
    }

    @Test
    fun `fetchWeather returns not found error when weather not found`() {
        val payload = FetchWeatherPayload("account id", BigDecimal.valueOf(0L), BigDecimal.valueOf(0L))
        val errorMessage = "weather not found"
        val expected = FetchWeatherError.NotFoundError(errorMessage)

        whenever(locationRepository.findFavourite(payload.accountId, payload.latitude, payload.longitude))
            .thenReturn(Result.ok(existingLocation))
        whenever(weatherRepository.findByCoordinates(payload.latitude, payload.longitude))
            .thenReturn(Result.error(FindWeatherError.NotFoundError(errorMessage)))

        val result = usecase.fetchWeather(payload)

        assertTrue(result.isError)
        assertEquals(expected, result.unwrapError())
    }

    @Test
    fun `fetchWeather returns internal error`() {
        val payload = FetchWeatherPayload("account id", BigDecimal.valueOf(0L), BigDecimal.valueOf(0L))
        val exception = Throwable("internal error")
        val expected = FetchWeatherError.InternalError(exception)

        whenever(locationRepository.findFavourite(payload.accountId, payload.latitude, payload.longitude))
            .thenReturn(Result.error(FindFavouriteError.InternalError(exception)))

        val result = usecase.fetchWeather(payload)

        assertTrue(result.isError)
        assertEquals(expected, result.unwrapError())
    }

    companion object {

        private const val CLOUDINESS = 0
        private const val CURRENT_TEMPERATURE = 0.0
        private const val FEELS_LIKE_TEMPERATURE = 0.0
        private const val HUMIDITY = 0
        private const val PRECIPITATION = 0.0
        private const val PRESSURE = 0.0
        private const val WEATHER_CODE = 0
        private const val WIND_DEGREE = 0
        private const val WIND_GUST = 0.0
        private const val WIND_SPEED = 0.0
    }
}
