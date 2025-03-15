package com.example.opticyou

import com.example.opticyou.communications.ServerRequests
import com.example.opticyou.communications.network.RetrofitClient
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockserver.integration.ClientAndServer
import org.mockserver.model.HttpRequest
import org.mockserver.model.HttpResponse

class MockServerTest {

    private lateinit var mockServer: ClientAndServer

    @Before
    fun startMockServer() {
        mockServer = ClientAndServer.startClientAndServer(1080) // Port on s'executarà MockServer
        RetrofitClient.setBaseUrlForTesting("http://localhost:1080/")
    }

    @After
    fun stopMockServer() {
        mockServer.stop()  // Aturem el servidor després dels tests
    }

    @Test
    fun loginUserSuccess() = runBlocking {
        mockServer
            .`when`(
                HttpRequest.request()
                    .withMethod("POST")
                    .withPath("/login")
            )
            .respond(
                HttpResponse.response()
                    .withStatusCode(200)
                    .withHeader("Content-Type", "application/json")
                    .withBody(
                        """
                        {
                            "success": true,
                            "role": "user",
                            "token": "tokenUser"
                        }
                    """.trimIndent()
                    )
            )

        val response = ServerRequests.login("user@optica.cat", "1234")

        assertEquals(true, response?.success)
        assertEquals("user", response?.rol)
        assertEquals("tokenUser", response?.token)
    }

    @Test
    fun loginAdminSuccess() = runBlocking {
        mockServer
            .`when`(
                HttpRequest.request()
                    .withMethod("POST")
                    .withPath("/login")
            )
            .respond(
                HttpResponse.response()
                    .withStatusCode(200)
                    .withHeader("Content-Type", "application/json")
                    .withBody(
                        """
                        {
                            "success": true,
                            "role": "admin",
                            "token": "tokenAdmin"
                        }
                    """.trimIndent()
                    )
            )

        val response = ServerRequests.login("admin@optica.cat", "1234")

        assertEquals(true, response?.success)
        assertEquals("admin", response?.rol)
        assertEquals("tokenAdmin", response?.token)
    }

    @Test
    fun logoutSuccess() = runBlocking {
        mockServer
            .`when`(
                HttpRequest.request()
                    .withMethod("POST")
                    .withPath("/logout")
            )
            .respond(
                HttpResponse.response()
                    .withStatusCode(200)
                    .withBody("true")
            )

        val result = ServerRequests.logout()

        assertEquals(true, result)
    }
}
