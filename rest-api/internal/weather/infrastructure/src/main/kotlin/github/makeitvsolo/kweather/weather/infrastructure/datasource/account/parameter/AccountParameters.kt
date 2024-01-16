package github.makeitvsolo.kweather.weather.infrastructure.datasource.account.parameter

import github.makeitvsolo.kweather.weather.domain.account.MapAccountInto

internal data class AccountParameters(
    val id: String,
    val name: String
) {

    object FromAccount : MapAccountInto<AccountParameters> {

        override fun from(id: String, name: String): AccountParameters =
            AccountParameters(id, name)
    }
}
