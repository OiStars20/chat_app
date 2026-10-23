package org.example.simple_chat_app

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "simple_chat_app",
    ) {
        App()
    }
}