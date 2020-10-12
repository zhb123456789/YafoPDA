package cn.com.yafo.yafopda.helper

import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

sealed class ClientBuilder {

    companion object {
        val plainClient: OkHttpClient by lazy {
            OkHttpClient
                .Builder()
                .connectTimeout(50, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build()
        }
    }

    fun client() : OkHttpClient {
        return plainClient
    }
}