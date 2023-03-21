package com.zapmap.pokemon.extensions

fun String.capitalize(): String {
    return lowercase().replaceFirstChar(Char::uppercase)
}