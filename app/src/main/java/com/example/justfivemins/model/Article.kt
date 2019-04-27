package com.example.justfivemins.model


data class Article(
    override var title: String,
    override var description: String,
    override var image: Int) : CellItem {
}