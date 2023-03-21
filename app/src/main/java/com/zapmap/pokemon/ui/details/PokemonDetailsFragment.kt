package com.zapmap.pokemon.ui.details

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
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import com.zapmap.pokemon.databinding.FragmentPokemonItemDetailsBinding
import com.zapmap.pokemon.ui.details.dto.Details
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class PokemonDetailsFragment : Fragment() {

    private val arguments: PokemonDetailsFragmentArgs by navArgs()
    private val viewModel: PokemonDetailsViewModel by viewModels()

    private var binding: FragmentPokemonItemDetailsBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPokemonItemDetailsBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }
        viewModel.uiState
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach(::takeSingleEvent)
            .launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.intent.fetchDetails(arguments.pokemonId)
    }

    private fun takeSingleEvent (uiState: PokemonDetailsViewModel.UiState) {
        binding?.pbPokemonImage?.visibility = if (uiState.isLoading) View.VISIBLE else View.GONE
        uiState.details?.let {
            updatePokemonDetails(it)
        }
        uiState.userMessage?.let {
            Toast.makeText(
                requireContext(),
                it,
                Toast.LENGTH_LONG
            ).show()
        }
    }
    fun updatePokemonDetails(details: Details) {
        binding?.apply {
            tvPokemonName.text = details.name
            tvWeight.text = details.weight
            tvHeight.text = details.height
            tvExperience.text = details.experience

            Glide.with(requireContext())
                .load(details.photo)
                .into(ivPokemonPhoto)

            cgPokemonTypes.removeAllViews()
            details.types.forEach {type ->
                val chip = Chip(requireContext())
                chip.text = type
                cgPokemonTypes.addView(chip)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}