package ch.wenksi.photosimilaritygame.datasource.sharedpreferences

import android.content.Context
import android.content.SharedPreferences
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import ch.wenksi.photosimilaritygame.domain.model.User
import timber.log.Timber

private const val PREFS_USER_USERNAME = "PREFS_USER_USERNAME"
private const val PREFS_FILE_NAME = "encrypted_shared_prefs"

class EncryptedUserSharedPreferences(context: Context) : UserSharedPreferences {

    private var cachedUser: User? = null

    private val sharedPreferences: SharedPreferences by lazy {
        val spec = KeyGenParameterSpec
            .Builder(
                MasterKey.DEFAULT_MASTER_KEY_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setKeySize(MasterKey.DEFAULT_AES_GCM_MASTER_KEY_SIZE)
            .build()

        val masterKey = MasterKey.Builder(context)
            .setKeyGenParameterSpec(spec)
            .build()

        EncryptedSharedPreferences.create(
            context,
            PREFS_FILE_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    override fun exists(): Boolean =
        (cachedUser != null || !decrypt(PREFS_USER_USERNAME).isNullOrEmpty())

    override fun read(): User {
        cachedUser?.let { return cachedUser!! }
        val userName = decrypt(PREFS_USER_USERNAME)

        if (userName.isNullOrEmpty()) {
            throw Error("No user found")
        }
        cachedUser = User(userName = userName)
        return cachedUser!!
    }

    override fun write(value: User) {
        encrypt(PREFS_USER_USERNAME, value.userName)
    }

    override fun clear() {
        cachedUser = null
        sharedPreferences.edit().remove(PREFS_USER_USERNAME).apply()
    }

    private fun encrypt(key: String, value: String) {
        Timber.d(
            "Encrypt and store to shared preferences {key: $key, value: $value"
        )
        sharedPreferences.edit().putString(key, value).apply()
    }

    private fun decrypt(key: String): String? {
        val value = sharedPreferences.getString(key, null)
        Timber.d(
            "Read and decrypt from shared preferences {key: $key, value: $value"
        )
        return value
    }
}
