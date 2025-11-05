package com.example.passwordmanager.ui.encryption

import android.os.Build
import android.util.Base64
import java.nio.ByteBuffer
import java.security.KeyStore
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

object CryptoManager {
    private const val ANDROID_KEYSTORE = "AndroidKeyStore"
    private const val KEY_ALIAS = "pm_app_aes_key"   // change if you like
    private const val TRANSFORMATION = "AES/GCM/NoPadding"
    private const val KEY_SIZE = 256 // bits
    private const val IV_SIZE_BYTES = 12
    private const val TAG_LENGTH_BITS = 128

    private val secureRandom = SecureRandom()

    @Synchronized
    fun ensureKeyExists() {
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE).apply { load(null) }
        if (!keyStore.containsAlias(KEY_ALIAS)) {
            val keyGenerator = KeyGenerator.getInstance("AES", ANDROID_KEYSTORE)
            val keyGenParameterSpecBuilder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                android.security.keystore.KeyGenParameterSpec.Builder(
                    KEY_ALIAS,
                    android.security.keystore.KeyProperties.PURPOSE_ENCRYPT or android.security.keystore.KeyProperties.PURPOSE_DECRYPT
                )
                    .setBlockModes(android.security.keystore.KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(android.security.keystore.KeyProperties.ENCRYPTION_PADDING_NONE)
                    .setKeySize(KEY_SIZE)
            } else {
                null
            }

            if (keyGenParameterSpecBuilder != null) {
                val keyGenSpec = keyGenParameterSpecBuilder.build()
                keyGenerator.init(keyGenSpec)
                keyGenerator.generateKey()
            } else {
                // API < 23: Keystore AES symmetric key generation isn't supported. Consider alternate approach or require API 23+.
                throw IllegalStateException("API < 23 not supported for AES Keystore symmetric key generation")
            }
        }
    }

    private fun getSecretKey(): SecretKey {
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE).apply { load(null) }
        val entry = keyStore.getEntry(KEY_ALIAS, null)
        if (entry !is KeyStore.SecretKeyEntry) {
            throw IllegalStateException("Not a SecretKeyEntry or key missing")
        }
        return entry.secretKey
    }

    /**
     * Encrypts plain text and returns a Base64 string containing (IV || ciphertext).
     * Stored format: 12 bytes IV + ciphertext. Entire byte[] is Base64-encoded.
     */
    fun encrypt(plainText: String): String {
        if (plainText.isEmpty()) return ""

        ensureKeyExists()
        val key = getSecretKey()
        val cipher = Cipher.getInstance(TRANSFORMATION)
        val iv = ByteArray(IV_SIZE_BYTES).also { secureRandom.nextBytes(it) }
        val spec = GCMParameterSpec(TAG_LENGTH_BITS, iv)
        cipher.init(Cipher.ENCRYPT_MODE, key, spec)
        val cipherText = cipher.doFinal(plainText.toByteArray(Charsets.UTF_8))

        // Combine IV + ciphertext
        val combined = ByteBuffer.allocate(iv.size + cipherText.size)
            .put(iv)
            .put(cipherText)
            .array()

        return Base64.encodeToString(combined, Base64.NO_WRAP)
    }

    /**
     * Decrypts the stored Base64 string and returns plaintext.
     */
    fun decrypt(base64Combined: String?): String {
        if (base64Combined.isNullOrEmpty()) return ""

        return try {
            val combined = Base64.decode(base64Combined, Base64.NO_WRAP)
            if (combined.size < IV_SIZE_BYTES) {
                // Not a valid encrypted payload — likely plaintext stored previously
                return base64Combined
            }

            val iv = combined.copyOfRange(0, IV_SIZE_BYTES)
            val cipherText = combined.copyOfRange(IV_SIZE_BYTES, combined.size)

            val key = getSecretKey()
            val cipher = Cipher.getInstance(TRANSFORMATION)
            val spec = GCMParameterSpec(TAG_LENGTH_BITS, iv)
            cipher.init(Cipher.DECRYPT_MODE, key, spec)
            val plainBytes = cipher.doFinal(cipherText)
            String(plainBytes, Charsets.UTF_8)
        } catch (e: Exception) {
            // Fallback if data is not actually encrypted
            base64Combined
        }
    }
}
