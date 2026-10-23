package org.example.simple_chat_app.server.chat

import kotlinx.serialization.json.Json
import org.example.simple_chat_app.ClientMessage
import org.example.simple_chat_app.ServerMessage
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@Component
class ChatWebSocketHandler : TextWebSocketHandler() {
    private val sessions = ConcurrentHashMap.newKeySet<WebSocketSession>()

    override fun afterConnectionEstablished(session: WebSocketSession) { sessions += session }
    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) { sessions -= session }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        when (val incoming = Json.decodeFromString<ClientMessage>(message.payload)) {
            is ClientMessage.Send -> broadcast(
                ServerMessage.NewMessage(
                    id = UUID.randomUUID().toString(),
                    sender = incoming.sender,
                    text = incoming.text,
                    timestamp = System.currentTimeMillis(),
                )
            )
        }
    }

    private fun broadcast(msg: ServerMessage) {
        val payload = TextMessage(Json.encodeToString<ServerMessage>(msg))
        sessions.forEach { s -> synchronized(s) { if (s.isOpen) s.sendMessage(payload) } }
    }
}