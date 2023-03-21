package com.zapmap.pokemon.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zapmap.pokemon.repository.PokemonRepository
import com.zapmap.pokemon.ui.list.dto.ItemState
import com.zapmap.pokemon.ui.list.mappers.ListItemsMapper
import com.zapmap.pokemon.ui.list.result.FetchPokemonListResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val pokemonRepository: PokemonRepository
) : ViewModel() {

    companion object {
        const val LIMIT = 50
    }

    data class UiState(
        val items: List<ItemState> = emptyList(),
        val isLoading: Boolean = false,
        val userMessage: String? = null
    )

    private val _singleEvent = MutableSharedFlow<UiState>(extraBufferCapacity = 32)
    val uiState: SharedFlow<UiState> = _singleEvent.asSharedFlow()


    val intent = object: ViewIntent {
        override fun fetchPokemons(offset: Int?) {
            viewModelScope.launch {
               pokemonRepository.fetchPokemons(limit = LIMIT, offset = offset ?: 0).collectLatest { result ->
                   when (result) {
                       is FetchPokemonListResult.Success -> {
                           _singleEvent.tryEmit(
                               UiState(
                                   isLoading = false,
                                   items =  ListItemsMapper.mapListItems(result.data)
                               ))
                       }
                       is FetchPokemonListResult.Loading -> {

                       }
                       is FetchPokemonListResult.Error.Exception -> {
                           delay(1000)
                           _singleEvent.tryEmit(
                               UiState(
                                   isLoading = false,
                                   userMessage = result.err?.message
                               ))
                       }
                       else -> {}
                   }
               }
            }
        }
    }

    interface ViewIntent {
        fun fetchPokemons(offset: Int? = null)
    }

}