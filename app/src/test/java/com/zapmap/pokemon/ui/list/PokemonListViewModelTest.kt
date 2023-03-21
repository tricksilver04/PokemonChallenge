package com.zapmap.pokemon.ui.list

import com.zapmap.pokemon.common.FakeNetworkDataSource
import com.zapmap.pokemon.common.MainCoroutineRule
import com.zapmap.pokemon.repository.DefaultPokemonRepository
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
class PokemonListViewModelTest {
    // Subject under test
    private lateinit var pokemonListViewModel: PokemonListViewModel

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
        pokemonListViewModel = PokemonListViewModel(pokemonRepository)
    }


    @Test
    fun loadPokemonListFromNetworkDataSource_Success() = runTest {
        // Set Main dispatcher to not run coroutines eagerly, for just this one test
        Dispatchers.setMain(StandardTestDispatcher())

        // Trigger loading of details
        pokemonListViewModel.intent.fetchPokemons()

        // Then verify 1 pokemon item + 1 loadmorestate item is on the list
        assertThat(pokemonListViewModel.uiState.first().items).hasSize(2)
    }


    @Test
    fun loadPokemonListFromNetworkDataSource_Error() = runTest {
        // Set Main dispatcher to not run coroutines eagerly, for just this one test
        Dispatchers.setMain(StandardTestDispatcher())

        // Setup Error
        fakeNetworkDataSource.shouldThrowError = true

        // Trigger loading of pokemon list
        pokemonListViewModel.intent.fetchPokemons()

        // Then error message is shown
        assertThat(pokemonListViewModel.uiState.first().userMessage?.equals("fake-exception"))
    }

}


