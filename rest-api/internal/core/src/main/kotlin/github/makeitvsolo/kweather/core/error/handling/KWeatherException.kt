package github.makeitvsolo.kweather.core.error.handling

abstract class KWeatherException : Throwable {

    protected constructor() : super()

    protected constructor(message: String) : super(message)

    protected constructor(throwable: Throwable) : super(throwable)
}
