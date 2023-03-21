package com.zapmap.pokemon.api.response

data class ListResponse (
    val count: Int,
    val next: String? = null,
    val previous: String? = null,
    val results: List<Item>
)

data class Item(
    val name: String,
    val url: String
)