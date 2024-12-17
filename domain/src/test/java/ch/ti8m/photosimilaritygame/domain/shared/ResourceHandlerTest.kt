package ch.wenksi.photosimilaritygame.domain.shared

import ch.wenksi.photosimilaritygame.domain.model.Resource
import ch.wenksi.photosimilaritygame.domain.model.ResourceHandler
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.security.InvalidKeyException

class ResourceHandlerTest {

    // region constants
    private val data = "test"

    // endregion constants

    lateinit var SUT: ResourceHandler

    @Before
    fun setUp() {
        SUT = ResourceHandler()
    }

    @Test
    fun handleSuccessReturnsSuccessResource() {
        val resource = SUT.handleSuccess(data)

        assertThat(resource).isInstanceOf(Resource.Success::class.java)
    }

    @Test
    fun handleSuccessReturnsCorrectData() {
        val resource = SUT.handleSuccess(data)
        val success = resource as Resource.Success

        assertThat(success.data).isEqualTo(data)
    }

    @Test
    fun handleExceptionReturnsNetworkErrorResourceWhenHostUnknown() {
        val resource: Resource<Any> = SUT.handleException(UnknownHostException())

        assertThat(resource).isInstanceOf(Resource.NetworkError::class.java)
    }

    @Test
    fun handleExceptionReturnsNetworkErrorResourceWhenSocketTimeouts() {
        val resource: Resource<Any> = SUT.handleException(SocketTimeoutException())

        assertThat(resource).isInstanceOf(Resource.NetworkError::class.java)
    }

    @Test
    fun handleExceptionReturnsServerErrorResourceWhenKeysAreInvalid() {
        val resource: Resource<Any> = SUT.handleException(InvalidKeyException())

        assertThat(resource).isInstanceOf(Resource.ServerError::class.java)
    }
}