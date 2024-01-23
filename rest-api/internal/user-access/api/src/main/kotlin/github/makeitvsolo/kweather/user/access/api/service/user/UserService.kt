package github.makeitvsolo.kweather.user.access.api.service.user

import github.makeitvsolo.kweather.user.access.api.service.user.usecase.AuthenticateUser
import github.makeitvsolo.kweather.user.access.api.service.user.usecase.AuthorizeUser
import github.makeitvsolo.kweather.user.access.api.service.user.usecase.RefreshAccessToken
import github.makeitvsolo.kweather.user.access.api.service.user.usecase.RegisterUser

interface UserService :
    RegisterUser,
    AuthorizeUser,
    AuthenticateUser,
    RefreshAccessToken
