package com.omtorney.snapcase.presenter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.omtorney.snapcase.data.model.Case
import com.omtorney.snapcase.databinding.ItemFavoriteBinding

class FavoritesAdapter(
    private val onItemClicked: (case: Case) -> Unit,
    private val onItemRemove: (case: Case) -> Unit,
) : RecyclerView.Adapter<FavoriteCaseViewHolder>() {

    private var cases: List<Case> = emptyList()

    fun setCases(cases: List<Case>) {
        this.cases = cases
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteCaseViewHolder {
        val binding = ItemFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteCaseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteCaseViewHolder, position: Int) {
        val item = cases.getOrNull(position)
        with(holder.binding) {
            item?.let {
                caseNumber.text = "Номер дела: ${item.number}"
                caseParticipants.text = "Участники: ${item.participants}"
            }
        }

        holder.binding.root.setOnClickListener {
            onItemClicked(cases[position])
        }

        holder.binding.buttonDelete.setOnClickListener {
            onItemRemove(cases[position])
            notifyItemRemoved(position)
        }
    }

    override fun getItemCount(): Int = cases.size
}

class FavoriteCaseViewHolder(val binding: ItemFavoriteBinding) : RecyclerView.ViewHolder(binding.root)