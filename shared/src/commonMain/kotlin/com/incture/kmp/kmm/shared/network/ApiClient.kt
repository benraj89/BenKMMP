package com.incture.kmp.kmm.shared.network

import com.incture.kmp.kmm.shared.entity.Event
import com.incture.kmp.kmm.shared.entity.StandardResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object ApiClient {

    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                useAlternativeNames = false
            })
        }
    }

    suspend fun getEvents(): StandardResponse<List<Event>> {
        return httpClient.get(EndPoints.GetEvents).body()
    }

    object EndPoints{
        private const val BASEURL="https://9a419e81-39f8-4f83-8f76-c0cf44f42c2d.mock.pstmn.io"
        const val GetEvents="$BASEURL/getEvents"
    }

}

