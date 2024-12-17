package ch.wenksi.photosimilaritygame.datasource.sharedpreferences

import ch.wenksi.photosimilaritygame.domain.model.Settings

interface SettingsSharedPreferences {
    /**
     * Returns the value, throws error if none exists.
     */
    fun read(): Settings

    /**
     * Stores the given value.
     */
    fun write(value: Settings)

    /**
     * Clears the value.
     */
    fun clear()
}