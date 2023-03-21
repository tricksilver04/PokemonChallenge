package com.zapmap.pokemon.ui.list.mappers

import com.zapmap.pokemon.api.response.DetailsResponse
import com.zapmap.pokemon.api.response.Item
import com.zapmap.pokemon.api.response.ListResponse
import com.zapmap.pokemon.ui.list.dto.ItemState
import org.junit.Assert
import org.junit.Test

class ListItemsMapperTest {

    @Test
    fun listItemsMapperTest(){
        val listItems = ListItemsMapper.mapListItems(response = ListResponse(
            count = 1,
            next = "https://pokeapi.co/api/v2/pokemon?offset=100&limit=50",
            previous = "https://pokeapi.co/api/v2/pokemon?offset=100&limit=50",
            results = listOf(Item(name = "fake-name",url = "https://pokeapi.co/api/v2/pokemon/2/"))
        ))
        Assert.assertTrue(listItems.size == 2) // test if 2 items are loaded ( PokemonItemState + LoadMoreState )
        Assert.assertTrue((listItems[0] as ItemState.PokemonItemState).name == "Fake-name") // test first letter is capitalized
        Assert.assertTrue((listItems[0] as ItemState.PokemonItemState).id == 2) // test if '2' is extracted successfully
    }
}