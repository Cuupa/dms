package com.cuupa.dms.service

import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.spec.InvalidKeySpecException
import java.security.spec.KeySpec
import java.util.*
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

class EncryptionService {

    private val decoder: Base64.Decoder = Base64.getDecoder()

    private val encoder: Base64.Encoder = Base64.getEncoder()

    @Throws(InvalidKeySpecException::class, NoSuchAlgorithmException::class)
    fun getEncryptedPassword(password: String, salt: String): String {
        val saltBytes = decoder.decode(salt)
        val spec: KeySpec = PBEKeySpec(password.toCharArray(), saltBytes, iterations, derivedKeyLength)
        val f = SecretKeyFactory.getInstance(algorithm)
        val encBytes = f.generateSecret(spec).encoded
        return encoder.encodeToString(encBytes)
    }

    @Throws(NoSuchAlgorithmException::class)
    fun generateSalt(): String {
        val random = SecureRandom.getInstance(hashAlgorithm)
        val salt = ByteArray(8)
        random.nextBytes(salt)
        return encoder.encodeToString(salt)
    }

    @get:Throws(NoSuchAlgorithmException::class)
    val accessToken: String
        get() {
            val random = SecureRandom.getInstance(hashAlgorithm)
            val randomBytes = ByteArray(24)
            random.nextBytes(randomBytes)
            return Base64.getEncoder().encodeToString(randomBytes)
        }

    companion object {
        private const val hashAlgorithm = "SHA1PRNG"
        private const val algorithm = "PBKDF2WithHmacSHA1"
        private const val derivedKeyLength = 160 // for SHA1
        private const val iterations = 20000 // NIST specifies 10000
    }
}