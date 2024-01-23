package github.makeitvsolo.kweather.weather.infrastructure.datasource.weather.cache.error

import github.makeitvsolo.kweather.core.error.handling.IntoThrowable

data class CleanCacheError internal constructor(private val throwable: Throwable) : IntoThrowable {

    override fun intoThrowable(): Throwable = throwable
}
