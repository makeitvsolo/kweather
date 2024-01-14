package github.makeitvsolo.kweather.user.access.api.service

import github.makeitvsolo.kweather.user.access.api.service.usecase.AuthorizeUser
import github.makeitvsolo.kweather.user.access.api.service.usecase.RegisterUser

interface UserService : RegisterUser, AuthorizeUser
