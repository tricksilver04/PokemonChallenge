package com.zapmap.pokemon.api.datasource

import com.zapmap.pokemon.api.response.DetailsResponse
import com.zapmap.pokemon.api.response.ListResponse

interface NetworkDataSource {
    suspend fun getPokemonList(
        limit: Int,
        offset: Int
    ): ListResponse

    suspend fun getDetails(pokemonId: Int): DetailsResponse
}