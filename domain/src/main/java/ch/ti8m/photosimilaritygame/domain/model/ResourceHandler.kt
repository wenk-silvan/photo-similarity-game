package ch.wenksi.photosimilaritygame.domain.model

import timber.log.Timber
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class ResourceHandler {

    fun <T : Any> handleSuccess(data: T): Resource<T> {
        return Resource.Success(data)
    }

    fun <T : Any> handleException(e: Exception): Resource<T> {
        Timber.e(e)
        return when (e) {
            is UnknownHostException, is SocketTimeoutException -> Resource.NetworkError()
            else -> Resource.ServerError()
        }
    }
}
