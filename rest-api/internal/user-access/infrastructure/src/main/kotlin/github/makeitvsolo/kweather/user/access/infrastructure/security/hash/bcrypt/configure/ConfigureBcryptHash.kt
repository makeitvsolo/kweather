package github.makeitvsolo.kweather.user.access.infrastructure.security.hash.bcrypt.configure

import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.user.access.infrastructure.security.hash.bcrypt.BcryptHash
import github.makeitvsolo.kweather.user.access.infrastructure.security.hash.bcrypt.error.BcryptHashConfigurationError

import at.favre.lib.crypto.bcrypt.BCrypt

class ConfigureBcryptHash private constructor(
    private var cost: Int? = null,
    private var salt: ByteArray? = null
) {

    fun cost(cost: Int): ConfigureBcryptHash = apply { this.cost = cost }
    fun salt(salt: ByteArray): ConfigureBcryptHash = apply { this.salt = salt }

    fun configured(): Result<BcryptHash, BcryptHashConfigurationError> {
        val appliedCost = cost
        val appliedSalt = salt

        appliedCost ?: return Result.error(BcryptHashConfigurationError.InvalidCostError("missing cost"))
        appliedSalt ?: return Result.error(BcryptHashConfigurationError.InvalidSaltError("missing salt"))

        if (appliedCost < BCrypt.MIN_COST || appliedCost > BCrypt.MAX_COST) {
            return Result.error(
                BcryptHashConfigurationError.InvalidCostError(
                    "cost must be between ${BCrypt.MIN_COST} and ${BCrypt.MAX_COST}"
                )
            )
        }

        if (appliedSalt.size != BCrypt.SALT_LENGTH) {
            return Result.error(
                BcryptHashConfigurationError.InvalidSaltError(
                    "salt must be ${BCrypt.SALT_LENGTH} length"
                )
            )
        }

        return Result.ok(BcryptHash(appliedCost, appliedSalt))
    }

    companion object {

        fun with(): ConfigureBcryptHash = ConfigureBcryptHash()
    }
}
