package com.example.notified.model

import java.io.Serializable

data class Notification(
    val packageName: String,
    val groupKey: String,
    val id: Int,
    val key: String,
    val isAppGroup: Boolean,
    val isClearable: Boolean,
    val isGroup: Boolean,
    val isOngoing: Boolean
) : Serializable
