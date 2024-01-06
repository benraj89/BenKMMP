package com.incture.kmp.kmm.shared

import com.incture.kmp.kmm.shared.cache.Database
import com.incture.kmp.kmm.shared.cache.DatabaseDriverFactory
import com.incture.kmp.kmm.shared.entity.Event
import com.incture.kmp.kmm.shared.network.ApiClient

class Repository(databaseDriverFactory: DatabaseDriverFactory) {

    private val database = Database(databaseDriverFactory)

    suspend fun getEvents(): List<Event> {

        val cachedLaunches = database.getAllEvents()
        if (cachedLaunches.isNotEmpty()) {
            return cachedLaunches
        } else {
            ApiClient.getEvents().also {
                database.clearEvents()
                database.createEvents(it.data)
                return it.data
            }
        }
    }
}
