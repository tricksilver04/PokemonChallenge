package com.zapmap.pokemon.ui.details.dto

data class Details(
    val name: String,
    val experience: String,
    val height: String,
    val weight: String,
    val photo: String,
    val types: List<String>
)