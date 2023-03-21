package com.zapmap.pokemon.ui.details.mappers

import com.zapmap.pokemon.api.response.DetailsResponse
import org.junit.Assert
import org.junit.Test

class DetailsMapperTest {
    @Test
    fun detailsMapperTest () {
        val details = DetailsMapper.mapDetails(response = DetailsResponse(
            name = "fake-name",
            weight = 1,
            height = 1,
            base_experience = 1,
            types = listOf(),
            sprites = DetailsResponse.Sprite(other = DetailsResponse.Other(home = DetailsResponse.Home(front_default = "fake-url")))
        ))
        Assert.assertTrue(details.name == "Fake-name") // Verify first letter is capitalized
        Assert.assertTrue(details.weight == "1 KG")
        Assert.assertTrue(details.height == "1 CM")
        Assert.assertTrue(details.experience == "1")
        Assert.assertTrue(details.types == listOf<String>())
        Assert.assertTrue(details.photo == "fake-url")
    }
}