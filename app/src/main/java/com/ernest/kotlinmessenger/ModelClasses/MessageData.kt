package com.ernest.kotlinmessenger.ModelClasses

data class MessageData (
    val message: String = "",
    val seen: Boolean = false,
    val time: Long = 0,
    val type: String = "",
    val userDisplayPic: String = "",
    val sender: String = ""
)


