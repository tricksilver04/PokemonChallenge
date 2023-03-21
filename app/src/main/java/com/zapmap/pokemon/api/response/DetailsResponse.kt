package com.zapmap.pokemon.api.response

data class DetailsResponse(
    val name: String,
    val weight: Int,
    val height: Int,
    val base_experience: Int,
    val types: List<TypeItem>,
    val sprites: Sprite
) {

    data class TypeItem(val type: Type)
    data class Type(val name: String, val url: String)
    data class Sprite(val other: Other)
    data class Other(val home: Home)
    data class Home(val front_default: String)
}