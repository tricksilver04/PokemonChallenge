package com.zapmap.pokemon.ui.list.result

import com.zapmap.pokemon.api.response.ListResponse

sealed class FetchPokemonListResult {

    object Loading: FetchPokemonListResult()

    class Success(
        val data: ListResponse
    ): FetchPokemonListResult()

    sealed class Error: FetchPokemonListResult() {

        class GlobalError(
            val code: String? = null,
            val message: String? = null,
        ): Error()

        class Exception(
            val err: Throwable? = null
        ): Error()

    }
}