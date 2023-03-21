package com.zapmap.pokemon.api.datasource

import com.zapmap.pokemon.api.response.DetailsResponse
import com.zapmap.pokemon.api.response.ListResponse
import com.zapmap.pokemon.api.service.ApiService
import javax.inject.Inject

class NetworkDataSourceImpl @Inject constructor(
    private val apiService: ApiService
) : NetworkDataSource {

    override suspend fun getPokemonList(
        limit: Int,
        offset: Int
    ): ListResponse {
        return apiService.fetchPokemons(
            limit,
            offset
        )
    }

    override suspend fun getDetails(pokemonId: Int): DetailsResponse {
        return apiService.fetchPokemonById(pokemonId)
    }
}