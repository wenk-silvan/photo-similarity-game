package ch.wenksi.photosimilaritygame.domain.model

sealed class Resource<out T> {

    data class Success<out T>(val data: T) : Resource<T>()
    class NetworkError<out T> : Resource<T>()
    class ServerError<out T> : Resource<T>()
    class InvalidCredentials<out T> : Resource<T>()
    class UserExists<out T> : Resource<T>()
    class NoSession<out T> : Resource<T>()
}
