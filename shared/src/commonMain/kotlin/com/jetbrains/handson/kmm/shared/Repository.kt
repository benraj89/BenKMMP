package com.jetbrains.handson.kmm.shared

import com.jetbrains.handson.kmm.shared.cache.Database
import com.jetbrains.handson.kmm.shared.cache.DatabaseDriverFactory
import com.jetbrains.handson.kmm.shared.entity.Event
import com.jetbrains.handson.kmm.shared.network.ApiClient

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
