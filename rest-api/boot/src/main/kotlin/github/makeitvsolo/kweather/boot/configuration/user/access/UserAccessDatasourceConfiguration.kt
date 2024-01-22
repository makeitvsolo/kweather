package github.makeitvsolo.kweather.boot.configuration.user.access

import github.makeitvsolo.kweather.user.access.api.datasource.user.UserRepository
import github.makeitvsolo.kweather.user.access.infrastructure.datasource.sql.configure.ConfigureSqlUserRepository

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

import javax.sql.DataSource

@Configuration
open class UserAccessDatasourceConfiguration {

    @Bean
    fun userRepository(datasource: DataSource): UserRepository =
        ConfigureSqlUserRepository.with()
            .datasource(datasource)
            .configured()
            .unwrapOrElseThrow { it.intoThrowable() }
}
