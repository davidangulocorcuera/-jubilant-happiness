package com.example.justfivemins.modules.messages.chat

import java.util.*

interface Message {
    var time: Date
    var senderId: String
}