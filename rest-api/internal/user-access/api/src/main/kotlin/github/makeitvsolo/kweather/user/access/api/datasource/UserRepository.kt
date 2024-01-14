package github.makeitvsolo.kweather.user.access.api.datasource

import github.makeitvsolo.kweather.user.access.api.datasource.operation.FindUser
import github.makeitvsolo.kweather.user.access.api.datasource.operation.SaveUser

interface UserRepository : SaveUser, FindUser
