package org.example

import app.main
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Request
import org.junit.Assert.assertEquals
import org.junit.Test
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.*


class HelloTest {

    private val client : OkHttpClient
    /**
     * Creates a trust manager that does not validate certificate chains.
     */
    init {

        val trustAllCerts = arrayOf<TrustManager>(
            object : X509TrustManager {
                override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}
                override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return arrayOf()
                }
            }
        )

        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, SecureRandom())

        val newBuilder = OkHttpClient.Builder()
        newBuilder.sslSocketFactory(sslContext.socketFactory, trustAllCerts[0] as X509TrustManager)
        newBuilder.hostnameVerifier { _: String?, _: SSLSession? -> true }

        client = newBuilder.build()
    }

    @Test
    fun `test if server supports http2`() {
        main()

        val response = client.newCall(Request.Builder().url("https://localhost:8443/").build()).execute()
        assertEquals(200, response.code)
        assertEquals(Protocol.HTTP_2, response.protocol)
    }

}
