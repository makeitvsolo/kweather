package github.makeitvsolo.kweather.user.access.api.datasource.user.operation

import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.user.access.api.datasource.user.error.SaveUserError
import github.makeitvsolo.kweather.user.access.domain.User

interface SaveUser {

    fun save(user: User): Result<Unit, SaveUserError>
}
