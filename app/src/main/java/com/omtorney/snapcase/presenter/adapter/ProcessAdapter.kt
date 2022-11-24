package com.omtorney.snapcase.presenter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.omtorney.snapcase.data.model.CaseProcess
import com.omtorney.snapcase.databinding.ItemProcessBinding

class ProcessAdapter(
//    private val onItemClicked: (news: String) -> Unit
) : RecyclerView.Adapter<ProcessViewHolder>() {

    private var process: MutableList<CaseProcess> = mutableListOf()

    fun setProcess(process: MutableList<CaseProcess>) {
        this.process = process
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProcessViewHolder {
        val binding = ItemProcessBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProcessViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProcessViewHolder, position: Int) {
        val item = process.getOrNull(position)
        with(holder.binding) {
            item?.let {
                event.text = it.toString()
            }
        }

//        holder.binding.root.setOnClickListener {
//            onItemClicked(newsList[position])
//        }
    }

    override fun getItemCount(): Int = process.size
}

class ProcessViewHolder(val binding: ItemProcessBinding) : RecyclerView.ViewHolder(binding.root)