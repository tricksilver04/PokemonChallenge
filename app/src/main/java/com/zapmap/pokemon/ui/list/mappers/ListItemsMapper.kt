package com.zapmap.pokemon.ui.list.mappers

import com.zapmap.pokemon.extensions.capitalize
import com.zapmap.pokemon.api.response.ListResponse
import com.zapmap.pokemon.ui.list.dto.ItemState
import com.zapmap.pokemon.ui.list.dto.ItemState.LoadMoreState
import com.zapmap.pokemon.ui.list.dto.ItemState.PokemonItemState
import java.net.URL

object ListItemsMapper {
    fun mapListItems(
        response: ListResponse
    ): List<ItemState> {
        val items = mutableListOf<ItemState>()
        /**
         * map name and id extracted
         * from the url
         * */
        items.addAll(
            response.results.map {
                val url = URL(it.url)
                val path = url.path
                val id = path.split("/")[4].toInt()
                PokemonItemState(
                    name = it.name.capitalize(),
                    id = id
                )
            }
        )
        // add LoadMoreState to trigger the next scroll loading of items
        items.add(LoadMoreState)
        return items
    }
}