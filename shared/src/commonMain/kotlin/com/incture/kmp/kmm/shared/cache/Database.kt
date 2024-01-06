package com.incture.kmp.kmm.shared.cache

import com.incture.kmp.kmm.shared.entity.Event


internal class Database(databaseDriverFactory: DatabaseDriverFactory) {

    private val database = AppDatabase(databaseDriverFactory.createDriver())
    private val dbQuery = database.appDatabaseQueries

    internal fun getAllEvents(): List<Event> {
        return dbQuery.selectAllEvents(::mapEventSelecting).executeAsList()
    }

    private fun insertEvent(event: Event) {
        dbQuery.insertEvent(
            id = event.id.toLong(),
            event = event.event,
            event_desc = event.event_desc,
            event_pic = event.event_pic,
            date = event.date,
            time = event.time,
            event_start = event.event_start
        )
    }

    internal fun createEvents(events: List<Event>) {
        dbQuery.transaction {
            events.forEach { event ->
                insertEvent(event)
            }
        }
    }

    internal fun clearEvents() {
        dbQuery.transaction {
            dbQuery.removeAllEvents()
        }
    }

    private fun mapEventSelecting(
        id: Long,
        event: String,
        event_desc: String,
        event_pic: String,
        date: String,
        time: String,
        event_start: String
    ): Event {
        return Event(
            id = id.toInt(),
            event = event,
            event_desc = event_desc,
            event_pic = event_pic,
            date = date,
            time=time,
            event_start=event_start
        )
    }
}