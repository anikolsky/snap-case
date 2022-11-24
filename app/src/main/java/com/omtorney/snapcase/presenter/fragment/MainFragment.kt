package com.omtorney.snapcase.presenter.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.omtorney.snapcase.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
//    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSchedule.setOnClickListener {
            if (binding.edittextSearch.text.isNullOrBlank()) {
                Snackbar.make(view, "Будет показано расписание на текущую дату", Snackbar.LENGTH_SHORT).show()
            }
            val action = MainFragmentDirections
                .actionMainFragmentToScheduleFragment(binding.edittextSchedule.text.toString())
            findNavController().navigate(action)
        }

        binding.buttonSearch.setOnClickListener {
            if (binding.edittextSearch.text.isNullOrBlank()) {
                Snackbar.make(view, "Введите имя участника или номер дела", Snackbar.LENGTH_SHORT)
                    .show()
            } else {
                val action = MainFragmentDirections
                    .actionMainFragmentToSearchFragment(binding.edittextSearch.text.toString())
                findNavController().navigate(action)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}