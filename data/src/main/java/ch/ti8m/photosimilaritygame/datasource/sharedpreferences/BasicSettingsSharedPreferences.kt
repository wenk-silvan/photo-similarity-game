package ch.wenksi.photosimilaritygame.datasource.sharedpreferences

import android.content.Context
import android.content.SharedPreferences
import ch.wenksi.photosimilaritygame.domain.model.Settings

private const val PREFS_USER_GENERATE_LOCALLY = "PREFS_USER_GENERATE_LOCALLY"
private const val PREFS_FILE_NAME = "settings_shared_prefs"

class BasicSettingsSharedPreferences(context: Context) : SettingsSharedPreferences {
    private var sharedPrefs: SharedPreferences? = null

    init {
        sharedPrefs = context.getSharedPreferences(
            PREFS_FILE_NAME,
            Context.MODE_PRIVATE,
        )
    }

    override fun read(): Settings {
        return Settings(
            generatePhotoLocally = sharedPrefs!!.getBoolean(PREFS_USER_GENERATE_LOCALLY, false),
        )
    }

    override fun write(value: Settings) {
        sharedPrefs!!.edit()
            .putBoolean(PREFS_USER_GENERATE_LOCALLY, value.generatePhotoLocally).apply()
    }

    override fun clear() {
        sharedPrefs!!.edit().remove(PREFS_USER_GENERATE_LOCALLY)
            .apply()
    }
}
