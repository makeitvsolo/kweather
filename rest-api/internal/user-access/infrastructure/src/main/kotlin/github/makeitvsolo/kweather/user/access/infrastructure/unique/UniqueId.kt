package github.makeitvsolo.kweather.user.access.infrastructure.unique

import github.makeitvsolo.kweather.core.type.Unique

import java.util.UUID

class UniqueId : Unique<String> {

    override fun unique(): String = UUID.randomUUID().toString()
}
