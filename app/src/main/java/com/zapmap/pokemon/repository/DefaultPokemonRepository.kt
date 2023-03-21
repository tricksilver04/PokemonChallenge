package com.zapmap.pokemon.repository

import com.zapmap.pokemon.api.datasource.NetworkDataSource
import com.zapmap.pokemon.ui.details.result.FetchPokemonDetailsResult
import com.zapmap.pokemon.ui.list.result.FetchPokemonListResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class DefaultPokemonRepository  @Inject constructor(
    private val networkDataSource: NetworkDataSource
) : PokemonRepository {

    override suspend fun fetchPokemons(limit: Int, offset: Int): Flow<FetchPokemonListResult> = flow {
        emit(FetchPokemonListResult.Loading)
        try {
            val response = networkDataSource.getPokemonList(limit,offset)
            emit(FetchPokemonListResult.Success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(FetchPokemonListResult.Error.Exception(e))
        }
    }

    override suspend fun fetchPokemonDetails(pokemonId: Int): Flow<FetchPokemonDetailsResult> = flow {
        emit(FetchPokemonDetailsResult.Loading)
        try {
            val response = networkDataSource.getDetails(pokemonId)
            emit(FetchPokemonDetailsResult.Success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(FetchPokemonDetailsResult.Error.Exception(e))
        }
    }

}