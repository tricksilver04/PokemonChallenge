package com.zapmap.pokemon.repository

import com.zapmap.pokemon.ui.details.result.FetchPokemonDetailsResult
import com.zapmap.pokemon.ui.list.result.FetchPokemonListResult
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {
    suspend fun fetchPokemons(
        limit: Int,
        offset: Int
    ): Flow<FetchPokemonListResult>

    suspend fun fetchPokemonDetails(pokemonId: Int): Flow<FetchPokemonDetailsResult>
}