package com.omtorney.snapcase.presenter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.omtorney.snapcase.data.model.Case
import com.omtorney.snapcase.databinding.ItemSearchBinding

class SearchAdapter(
    private val onItemClicked: (case: Case) -> Unit
) : RecyclerView.Adapter<SearchCaseViewHolder>() {

    private var cases: List<Case> = emptyList()

    fun setCases(cases: List<Case>) {
        this.cases = cases
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchCaseViewHolder {
        val binding = ItemSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchCaseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchCaseViewHolder, position: Int) {
        val item = cases.getOrNull(position)
        with(holder.binding) {
            iconActText.isVisible = item?.actTextUrl!!.isNotEmpty()
            item?.let {
                caseNumber.text = "Номер дела: ${item.number}"
                caseParticipants.text = "Участники: ${item.participants}"
            }
        }

        holder.binding.root.setOnClickListener {
            onItemClicked(cases[position])
        }
    }

    override fun getItemCount() = cases.size
}

class SearchCaseViewHolder(val binding: ItemSearchBinding) : RecyclerView.ViewHolder(binding.root)