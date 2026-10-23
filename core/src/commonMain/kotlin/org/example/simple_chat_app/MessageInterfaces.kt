package org.example.simple_chat_app

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface ClientMessage {
    @Serializable @SerialName("send")
    data class Send(val sender: String, val text: String) : ClientMessage
}

@Serializable
sealed interface ServerMessage {
    @Serializable @SerialName("new_Message")
    data class NewMessage(
        val id: String,
        val sender: String,
        val text: String,
        val timestamp: Long
    ) : ServerMessage
}