package github.makeitvsolo.kweather.user.access.infrastructure.datasource.sql.error

import github.makeitvsolo.kweather.core.error.handling.IntoThrowable

data class TruncateTableError internal constructor(private val throwable: Throwable) : IntoThrowable {

    override fun intoThrowable(): Throwable = throwable
}
