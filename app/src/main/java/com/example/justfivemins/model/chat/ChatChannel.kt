package com.example.justfivemins.model.chat

class ChatChannel(var userIds: MutableList<String>) {
    constructor(): this(mutableListOf())
}