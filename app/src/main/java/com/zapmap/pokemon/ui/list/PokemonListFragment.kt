package com.zapmap.pokemon.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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

        // setup handling of UI events and add lifecyclescope to avoid memory leaks
        viewModel.uiState
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach(::takeSingleEvent)
            .launchIn(viewLifecycleOwner.lifecycleScope)

        // prevent this fragment from being recreated when back button is tapped
        if (!hasInitializedRootView) {
            hasInitializedRootView = true

            binding?.apply {
                initPokemonListScrollListener(rcvPokemonList)
            }
            viewModel.intent.fetchPokemons()
        }
    }

    /**
     * handle the UI Events and update UI
     * */
    private fun takeSingleEvent (uiState: PokemonListViewModel.UiState) {
        if (uiState.isLoading) {
            binding?.pbFetchingPokemons?.visibility = View.VISIBLE
            binding?.rcvPokemonList?.visibility = View.GONE
            binding?.flEmptyList?.visibility = View.GONE
            binding?.btnRetry?.setOnClickListener {
                viewModel.intent.fetchPokemons()
            }
        } else if (uiState.userMessage!=null) {
            Toast.makeText(requireContext(),"Error: ${uiState.userMessage}",Toast.LENGTH_SHORT).show()
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
            updateItems(uiState.items)
        }
    }

    /**
     * Setup PokemonAdapter with the callbacks
     * */
    private val pokemonAdapter = PokemonAdapter(
        retryCallback = {
            viewModel.intent.fetchPokemons(nextOffset)
        },
        itemClickCallback = { id ->
            // open the Pokemon details screen
            findNavController().navigate(
                PokemonListFragmentDirections.mainToPokemonDetails(pokemonId = id)
            )
        }
    )

    /**
     *  Initialize recyclerview scrolllistener and setup conditions when to add new items
     *  to the list
     * */
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
                        // call fetchPokemons only when there exist a visible LoadMoreState at the bottom of the list
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

    fun updateItems(items: List<ItemState>) {
        pokemonAdapter.processItems(items)
        // increment nextOffset variable after the recyclerview list is updated
        nextOffset += PokemonListViewModel.LIMIT
    }
}




