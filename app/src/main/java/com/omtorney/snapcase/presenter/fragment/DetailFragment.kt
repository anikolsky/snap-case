package com.omtorney.snapcase.presenter.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.omtorney.snapcase.data.State
import com.omtorney.snapcase.databinding.FragmentDetailBinding
import com.omtorney.snapcase.domain.usecase.FillCaseUseCase
import com.omtorney.snapcase.domain.usecase.SaveCaseUseCase
import com.omtorney.snapcase.presenter.adapter.ProcessAdapter
import com.omtorney.snapcase.presenter.viewmodel.DetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    @Inject lateinit var saveCaseUseCase: SaveCaseUseCase
    @Inject lateinit var fillCaseUseCase: FillCaseUseCase

    private val args: DetailFragmentArgs by navArgs()
    private val viewModel: DetailViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return DetailViewModel(fillCaseUseCase, saveCaseUseCase) as T
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO А если пришло из БД? Сразу будет обновлять

        viewModel.fillCase(args.case)

        val processAdapter = ProcessAdapter()
        binding.processRecyclerview.adapter = processAdapter

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.case.collect { case ->
                with(binding) {
                    caseNumber.text = case.number
                    caseHearingDateTime.text = case.hearingDateTime
                    caseCategory.text = case.category
                    caseParticipants.text = case.participants
                    caseJudge.text = case.judge
                    caseActDateTime.text = case.actDateTime
                    caseReceiptDate.text = case.receiptDate
                    caseResult.text = case.result
                    caseActDateForce.text = case.actDateForce
                    caseActTextUrl.text = case.actTextUrl
                    appeal.append(case.appealToString())
                }
                processAdapter.setProcess(case.process)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.isLoading.collect { binding.progress.isVisible = it }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.state.collect { state ->
                when(state) {
                    State.Default -> {}
                    State.Success -> Toast.makeText(context, "Дело добавлено в избранное", Toast.LENGTH_SHORT).show()
                    is State.Failure -> Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.buttonSave.setOnClickListener {
            viewModel.saveCase(args.case)
        }

        if (args.case.actTextUrl.isNotEmpty()) {
            binding.buttonShowAct.isVisible = true
            binding.buttonShowAct.setOnClickListener {
                val action = DetailFragmentDirections.actionDetailFragmentToActFragment(args.case.actTextUrl)
                findNavController().navigate(action)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}