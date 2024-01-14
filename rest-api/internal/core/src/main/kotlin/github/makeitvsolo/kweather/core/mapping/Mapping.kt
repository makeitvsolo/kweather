package github.makeitvsolo.kweather.core.mapping

interface Into<out R>

internal interface AnyObject {

    fun <R, F : Into<R>> into(map: F)
}

internal interface From {

    object FromAnyObject : Into<AnyObject>
}
