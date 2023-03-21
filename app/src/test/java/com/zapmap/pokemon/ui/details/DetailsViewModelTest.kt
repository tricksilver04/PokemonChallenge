package com.zapmap.pokemon.ui.details

import com.zapmap.pokemon.common.MainCoroutineRule
import com.zapmap.pokemon.repository.DefaultPokemonRepository
import com.zapmap.pokemon.common.FakeNetworkDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.assertj.core.api.Assertions.assertThat


/**
 * Unit tests for the implementation of [PokemonListViewModel]
 */
@ExperimentalCoroutinesApi
class DetailsViewModelTest {
    // Subject under test
    private lateinit var pokemonDetailsViewModel: PokemonDetailsViewModel

    // Use a fake Network DataSource to be injected to the repository
    private lateinit var fakeNetworkDataSource : FakeNetworkDataSource
    // Use a repository to be injected into the viewmodel
    private lateinit var pokemonRepository: DefaultPokemonRepository

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setupViewModel() {
        fakeNetworkDataSource = FakeNetworkDataSource()
        pokemonRepository = DefaultPokemonRepository(fakeNetworkDataSource)
        pokemonDetailsViewModel = PokemonDetailsViewModel(pokemonRepository)
    }


    @Test
    fun loadDetailsFromNetworkSource_Success() = runTest {
        // Set Main dispatcher to not run coroutines eagerly, for just this one test
        Dispatchers.setMain(StandardTestDispatcher())

        // Trigger loading of details
        pokemonDetailsViewModel.intent.fetchDetails(id = 1)

        // Then pokemon name is verified as correct
        assertThat(pokemonDetailsViewModel.uiState.first().details?.name == "fake-name")

    }

    @Test
    fun loadDetailsFromNetworkSource_Error() = runTest {
        // Set Main dispatcher to not run coroutines eagerly, for just this one test
        Dispatchers.setMain(StandardTestDispatcher())

        // Setup error
        fakeNetworkDataSource.shouldThrowError = true

        // Trigger loading of details
        pokemonDetailsViewModel.intent.fetchDetails(id = 1)

        // Then error message is shown
        assertThat(pokemonDetailsViewModel.uiState.first().userMessage?.equals("fake-exception"))

        // Loading is stopped
        assertThat(!pokemonDetailsViewModel.uiState.first().isLoading)
    }

}


