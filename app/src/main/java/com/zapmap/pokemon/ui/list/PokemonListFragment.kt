package com.zapmap.pokemon.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zapmap.pokemon.databinding.FragmentPokemonListBinding
import com.zapmap.pokemon.ui.list.adapters.PokemonAdapter
import com.zapmap.pokemon.ui.list.dto.ItemState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

@AndroidEntryPoint
class PokemonListFragment : Fragment() {

    private val viewModel: PokemonListViewModel by viewModels()
    private var binding : FragmentPokemonListBinding? = null

    var hasInitializedRootView = false
    private var rootView: View? = null

    fun getPersistentView(): View? {
        if (rootView == null) {
            binding = FragmentPokemonListBinding.inflate(layoutInflater)
            rootView = binding?.root
        } else {
            (rootView?.parent as? ViewGroup)?.removeView(rootView)
        }
        return rootView
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return getPersistentView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.uiState
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach(::takeSingleEvent)
            .launchIn(viewLifecycleOwner.lifecycleScope)

        if (!hasInitializedRootView) {
            hasInitializedRootView = true

            binding?.apply {
                initPokemonListScrollListener(rcvPokemonList)
            }
            viewModel.intent.fetchPokemons()
        }
    }

    private fun takeSingleEvent (uiState: PokemonListViewModel.UiState) {
        if (uiState.isLoading) {
            binding?.pbFetchingPokemons?.visibility = View.VISIBLE
            binding?.rcvPokemonList?.visibility = View.GONE
            binding?.flEmptyList?.visibility = View.GONE
            binding?.btnRetry?.setOnClickListener {
                viewModel.intent.fetchPokemons()
            }
        } else if (uiState.userMessage!=null) {
            if (pokemonAdapter.items.isEmpty()){
                binding?.pbFetchingPokemons?.visibility = View.GONE
                binding?.rcvPokemonList?.visibility = View.GONE
                binding?.flEmptyList?.visibility = View.VISIBLE
                binding?.btnRetry?.setOnClickListener {
                    viewModel.intent.fetchPokemons()
                }
            } else {
                pokemonAdapter.setRetryState()
            }
        } else if (uiState.items!=null) {
            binding?.pbFetchingPokemons?.visibility = View.VISIBLE
            binding?.rcvPokemonList?.visibility = View.VISIBLE
            binding?.flEmptyList?.visibility = View.GONE
            addItems(uiState.items)
        }
    }

    private val pokemonAdapter = PokemonAdapter(
        retryCallback = {
            viewModel.intent.fetchPokemons(nextOffset)
        },
        itemClickCallback = { id ->
            findNavController().navigate(
                PokemonListFragmentDirections.mainToPokemonDetails(pokemonId = id)
            )
        }
    )

    private fun initPokemonListScrollListener(recyclerView: RecyclerView) {
        with(recyclerView) {
            adapter = pokemonAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(
                    recyclerView: RecyclerView,
                    dx: Int,
                    dy: Int
                ) {
                    val lastVisibleItemPosition = (recyclerView.layoutManager as LinearLayoutManager)
                        .findLastCompletelyVisibleItemPosition()
                    if (lastVisibleItemPosition != RecyclerView.NO_POSITION && dy > 0) {
                        (pokemonAdapter.items[lastVisibleItemPosition] as? ItemState.LoadMoreState)?.let {
                            viewModel.intent.fetchPokemons(nextOffset)
                            Timber.e("nextoffset ${nextOffset}")
                        }
                    }
                }
            })
        }
    }

    var nextOffset = 0

    fun addItems(items: List<ItemState>) {
        pokemonAdapter.processItems(items)
        nextOffset += PokemonListViewModel.LIMIT
    }
}




