package ch.wenksi.photosimilaritygame.domain.model

data class User(
    val userName: String,
) {
    override fun toString(): String {
        return "[userName=$userName]"
    }
}