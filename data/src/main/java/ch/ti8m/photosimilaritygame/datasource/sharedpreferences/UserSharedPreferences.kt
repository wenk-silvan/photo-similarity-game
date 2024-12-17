package ch.wenksi.photosimilaritygame.datasource.sharedpreferences

import ch.wenksi.photosimilaritygame.domain.model.User

interface UserSharedPreferences {
    /**
     * Returns the value, throws error if none exists.
     */
    fun read(): User

    /**
     * Stores the given value.
     */
    fun write(value: User)

    /**
     * Clears the value.
     */
    fun clear()

    /**
     * Returns whether the value exists or not.
     */
    fun exists(): Boolean
}