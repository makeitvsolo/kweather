rootProject.name = "kweather-api"

include("internal:core")

include("internal:weather")
include("internal:weather:domain")
include("internal:weather:api")

include("internal:user-access")
include("internal:user-access:domain")
