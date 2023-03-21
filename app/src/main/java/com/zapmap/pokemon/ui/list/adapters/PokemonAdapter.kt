package com.zapmap.pokemon.ui.list.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.zapmap.pokemon.R
import com.zapmap.pokemon.databinding.LayoutListItemBinding
import com.zapmap.pokemon.databinding.LayoutLoadingMoreItemBinding
import com.zapmap.pokemon.databinding.LayoutRetryItemBinding
import com.zapmap.pokemon.ui.list.dto.ItemState
import com.zapmap.pokemon.ui.list.dto.ItemState.*

class PokemonAdapter(
    val items: MutableList<ItemState> = mutableListOf(),
    private val retryCallback: (() -> Unit)? = null,
    private val itemClickCallback: ((id: Int) -> Unit)? = null
): RecyclerView.Adapter<PokemonAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            when (viewType) {
                R.layout.layout_loading_more_item -> LayoutLoadingMoreItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                R.layout.layout_retry_item -> LayoutRetryItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                else -> LayoutListItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            }
        )
    }

    private fun setLoadMoreState() {
        putItems(items.filter { it is PokemonItemState }  + (LoadMoreState))
    }

    override fun onBindViewHolder(
        viewHolder: ViewHolder,
        position: Int
    ) {
        when (val item = items[position]) {
            is RetryState -> {
                (viewHolder.viewBinding as LayoutRetryItemBinding).btnRetry.setOnClickListener {
                    retryCallback?.invoke()
                    setLoadMoreState()
                }
            }
            is PokemonItemState -> {
                with(viewHolder.viewBinding as LayoutListItemBinding) {
                    tvName.text = item.name
                    root.setOnClickListener {
                        itemClickCallback?.invoke(item.id)
                    }
                }
            }
            else -> {}
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is LoadMoreState -> R.layout.layout_loading_more_item
            is RetryState -> R.layout.layout_retry_item
            else -> R.layout.layout_list_item
        }
    }

    fun setRetryState() {
        putItems(items.filter { it is PokemonItemState }  + (RetryState))
    }

    override fun getItemCount() = items.size

    inner class ViewHolder(val viewBinding: ViewBinding) : RecyclerView.ViewHolder(viewBinding.root)

    class AdapterDiffUtil(
        val oldItems: List<ItemState>,
        val newItems: List<ItemState>
    ) : DiffUtil.Callback() {

        override fun getOldListSize() = oldItems.size

        override fun getNewListSize() = newItems.size

        override fun areItemsTheSame(
            oldItemPosition: Int,
            newItemPosition: Int
        ): Boolean {
            return oldItems[oldItemPosition] == newItems[newItemPosition]
        }

        override fun areContentsTheSame(
            oldItemPosition: Int,
            newItemPosition: Int
        ): Boolean {
            return oldItems[oldItemPosition] == newItems[newItemPosition]
        }
    }

    fun processItems(newItems: List<ItemState>){
        val combinedItems = items.filter { it is PokemonItemState } + (newItems)
        putItems(combinedItems)
    }

    fun putItems(newItems: List<ItemState>) {
        val diffResult = DiffUtil.calculateDiff(
            AdapterDiffUtil(
                oldItems = items,
                newItems = newItems
            )
        )
        this.items.clear()
        this.items.addAll(newItems)
        diffResult.dispatchUpdatesTo(this)
    }
}