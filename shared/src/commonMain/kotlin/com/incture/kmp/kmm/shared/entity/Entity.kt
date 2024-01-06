package com.incture.kmp.kmm.shared.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Event(
    @SerialName("id")
    val id: Int,
    @SerialName("event")
    val event: String,
    @SerialName("event_desc")
    val event_desc: String,
    @SerialName("event_pic")
    val event_pic: String,
    @SerialName("date")
    val date: String,
    @SerialName("time")
    val time: String,
    @SerialName("event_start")
    val event_start:String,
)

@Serializable
data class StandardResponse<T>(
    @SerialName("status")
    val status: Boolean,
    @SerialName("message")
    val message: String,
    @SerialName("data")
    val data:T
)