package com.example.justfivemins.model.chat

import java.util.*

class TextMessage(var text: String = "", override var time: Date = Date(), override var senderId: String = ""):
    Message {
}