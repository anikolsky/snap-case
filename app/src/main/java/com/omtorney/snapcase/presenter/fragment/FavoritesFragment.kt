package com.omtorney.snapcase.presenter.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.omtorney.snapcase.data.local.CaseDao
import com.omtorney.snapcase.databinding.FragmentFavoritesBinding
import com.omtorney.snapcase.domain.usecase.DeleteCaseUseCase
import com.omtorney.snapcase.presenter.adapter.FavoritesAdapter
import com.omtorney.snapcase.presenter.viewmodel.FavoritesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    @Inject lateinit var caseDao: CaseDao
    @Inject lateinit var deleteCaseUseCase: DeleteCaseUseCase

    private val viewModel: FavoritesViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return FavoritesViewModel(caseDao, deleteCaseUseCase) as T
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO Надо загружать из БД
        val adapter = FavoritesAdapter(
            {
                val action = FavoritesFragmentDirections.actionFavoritesFragmentToDetailFragment(it)
                findNavController().navigate(action)
            },
            {
                CoroutineScope(Dispatchers.IO).launch { viewModel.deleteCase(it) }
            }
        )

        binding.recyclerView.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.allCases.collect {
                adapter.setCases(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}