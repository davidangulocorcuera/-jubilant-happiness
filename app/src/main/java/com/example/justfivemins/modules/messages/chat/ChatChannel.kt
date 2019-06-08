package com.example.justfivemins.modules.messages.chat

class ChatChannel(var userIds: MutableList<String>) {
    constructor(): this(mutableListOf())
}