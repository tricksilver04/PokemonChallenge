package com.zapmap.pokemon.ui.details.mappers

import com.zapmap.pokemon.extensions.capitalize
import com.zapmap.pokemon.api.response.DetailsResponse
import com.zapmap.pokemon.ui.details.dto.Details

object DetailsMapper {

    fun mapDetails(response: DetailsResponse): Details {
        return Details(
            name = response.name.capitalize(),
            experience = response.base_experience.toString(),
            types = response.types.map { it.type.name },
            photo = response.sprites.other.home.front_default,
            height = "${response.height} CM",
            weight = "${response.weight} KG"
        )
    }
}