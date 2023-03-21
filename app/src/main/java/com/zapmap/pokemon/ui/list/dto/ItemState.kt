package com.zapmap.pokemon.ui.list.dto

sealed class ItemState {
    data class PokemonItemState(
        val name: String,
        val id: Int
    ) : ItemState()
    object LoadMoreState : ItemState()
    object RetryState : ItemState()
}