package com.zapmap.pokemon.ui.details.result

import com.zapmap.pokemon.api.response.DetailsResponse

sealed class FetchPokemonDetailsResult {

    object Loading: FetchPokemonDetailsResult()

    class Success(
        val data: DetailsResponse
    ): FetchPokemonDetailsResult()

    sealed class Error: FetchPokemonDetailsResult() {

        class GlobalError(
            val code: String? = null,
            val message: String? = null,
        ): Error()

        class Exception(
            val err: Throwable? = null
        ): Error()

    }
}