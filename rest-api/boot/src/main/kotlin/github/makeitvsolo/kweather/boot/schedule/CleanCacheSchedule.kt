package github.makeitvsolo.kweather.boot.schedule

import github.makeitvsolo.kweather.weather.infrastructure.datasource.weather.cache.CachedWeatherRepository

import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
open class CleanCacheSchedule(
    private val repository: CachedWeatherRepository
) {

    @Scheduled(cron = "\${schedule.cache-clean.cron.expression}", zone = "\${schedule.cache-clean.cron.zone}")
    open fun cleanCache() {
        log.info("start cleaning cache...")

        repository.cleanCache().ifOk {
            log.info("cache cleaned successfully")
        }.ifError { error ->
            log.info("clean cache error: $error")
        }
    }

    companion object {
        private val log = LoggerFactory.getLogger(CleanCacheSchedule::class.java)
    }
}
