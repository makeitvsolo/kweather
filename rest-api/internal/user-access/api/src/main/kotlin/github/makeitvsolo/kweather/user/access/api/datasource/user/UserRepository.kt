package github.makeitvsolo.kweather.user.access.api.datasource.user

import github.makeitvsolo.kweather.user.access.api.datasource.user.operation.FindUser
import github.makeitvsolo.kweather.user.access.api.datasource.user.operation.SaveUser

interface UserRepository : SaveUser, FindUser
