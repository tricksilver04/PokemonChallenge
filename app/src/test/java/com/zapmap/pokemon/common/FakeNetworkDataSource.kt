package com.zapmap.pokemon.common

import com.zapmap.pokemon.BuildConfig
import com.zapmap.pokemon.api.datasource.NetworkDataSource
import com.zapmap.pokemon.api.response.DetailsResponse
import com.zapmap.pokemon.api.response.ListResponse
import com.zapmap.pokemon.api.response.Item

class FakeNetworkDataSource: NetworkDataSource {

    var shouldThrowError = false
    var fetchSomeItems = true

    override suspend fun getPokemonList(limit: Int, offset: Int): ListResponse {
        if (shouldThrowError)
            throw Exception("fake-exception")

        if (!fetchSomeItems)
            return ListResponse(count = 0, next = null, results = emptyList())

        return ListResponse(count = 1, next = null, results = listOf(
            Item(name = "fake-name", url = BuildConfig.SERVER_API.plus("pokemon/112"))
        ))
    }

    override suspend fun getDetails(pokemonId: Int): DetailsResponse {
        if (shouldThrowError)
            throw Exception("fake-exception")

        if (!fetchSomeItems)
            return DetailsResponse(name = "fake-name",
                height = -1,
                weight = -1,
                base_experience = -1,
                sprites = DetailsResponse.Sprite(DetailsResponse.Other(DetailsResponse.Home(""))),
                types = emptyList())

        return DetailsResponse(name = "fake-name",
            height = -1,
            weight = -1,
            base_experience = -1,
            sprites = DetailsResponse.Sprite(DetailsResponse.Other(DetailsResponse.Home(""))),
            types = listOf(DetailsResponse.TypeItem(type = DetailsResponse.Type(name = "fake-name", url = "fake-url")))
        )
    }
}