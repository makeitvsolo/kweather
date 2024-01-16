package github.makeitvsolo.kweather.weather.application

import org.mockito.MockitoAnnotations.openMocks
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

abstract class ApplicationUnitTest {

    private lateinit var closeable: AutoCloseable

    @BeforeTest
    fun beforeEach() {
        closeable = openMocks(this)
    }

    @AfterTest
    fun afterEach() {
        closeable.close()
    }
}
