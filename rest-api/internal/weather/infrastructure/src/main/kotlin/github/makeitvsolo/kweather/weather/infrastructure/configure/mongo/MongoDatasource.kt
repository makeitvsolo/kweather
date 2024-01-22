package github.makeitvsolo.kweather.weather.infrastructure.configure.mongo

import com.mongodb.client.MongoDatabase

class MongoDatasource internal constructor(private val database: MongoDatabase) {

    internal fun intoDatabase(): MongoDatabase = database
}
