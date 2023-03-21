package com.zapmap.pokemon.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zapmap.pokemon.ui.details.dto.Details
import com.zapmap.pokemon.ui.details.mappers.DetailsMapper
import com.zapmap.pokemon.repository.PokemonRepository
import com.zapmap.pokemon.ui.details.result.FetchPokemonDetailsResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonDetailsViewModel @Inject constructor(
    private val pokemonRepository: PokemonRepository
) : ViewModel() {

    data class UiState(
        val details: Details? = null,
        val isLoading: Boolean = false,
        val userMessage: String? = null
    )

    private val _singleEvent = MutableSharedFlow<UiState>(extraBufferCapacity = 32)
    val uiState: SharedFlow<UiState> = _singleEvent.asSharedFlow()


    val intent = object: ViewIntent {
        override fun fetchDetails(id: Int) {
            viewModelScope.launch {
                pokemonRepository.fetchPokemonDetails(id).collectLatest { result ->
                    when (result) {
                        is FetchPokemonDetailsResult.Success -> {
                            _singleEvent.tryEmit(
                                UiState(
                                    isLoading = false,
                                    details = DetailsMapper.mapDetails(result.data)
                                ))
                        }
                        is FetchPokemonDetailsResult.Loading -> {
                            _singleEvent.tryEmit(
                                UiState(
                                    isLoading = true
                                ))
                        }
                        is FetchPokemonDetailsResult.Error.Exception -> {
                            delay(1000)
                            _singleEvent.tryEmit(
                                UiState(
                                    isLoading = false,
                                    userMessage = result.err?.message
                                )
                            )
                        }
                        else -> {}
                    }
                }
            }
        }
    }

    interface ViewIntent {
        fun fetchDetails(id: Int)
    }

}