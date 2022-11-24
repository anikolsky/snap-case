package com.omtorney.snapcase.presenter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.omtorney.snapcase.data.model.Case
import com.omtorney.snapcase.databinding.ItemScheduleBinding

class ScheduleAdapter(
    private val onItemClicked: (case: Case) -> Unit
) : RecyclerView.Adapter<ScheduleCaseViewHolder>() {

    private var cases: List<Case> = emptyList()

    fun setCases(cases: List<Case>) {
        this.cases = cases
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleCaseViewHolder {
        val binding = ItemScheduleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ScheduleCaseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ScheduleCaseViewHolder, position: Int) {
        val item = cases.getOrNull(position)
        with(holder.binding) {
            iconActText.isVisible = item?.actTextUrl!!.isNotEmpty()
            item?.let {
                caseHearingDateTime.text = "Время заседания: ${item.hearingDateTime}"
                caseParticipants.text = "Участники: ${item.participants}"
            }
        }

        holder.binding.root.setOnClickListener {
            onItemClicked(cases[position])
        }
    }

    override fun getItemCount() = cases.size
}

class ScheduleCaseViewHolder(val binding: ItemScheduleBinding) : RecyclerView.ViewHolder(binding.root)