package com.zapmap.pokemon.api.service

import com.zapmap.pokemon.api.response.DetailsResponse
import com.zapmap.pokemon.api.response.ListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("pokemon")
    suspend fun fetchPokemons(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): ListResponse

    @GET("pokemon/{id}")
    suspend fun fetchPokemonById(
        @Path("id") id: Int,
    ): DetailsResponse
}