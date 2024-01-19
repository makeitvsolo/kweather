package github.makeitvsolo.kweather.user.access.api.datasource.user.operation

import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.user.access.api.datasource.user.error.FindUserError
import github.makeitvsolo.kweather.user.access.domain.User

interface FindUser {

    fun findByName(name: String): Result<User, FindUserError>
}
