package com.omtorney.snapcase.presenter.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.omtorney.snapcase.databinding.FragmentActBinding
import com.omtorney.snapcase.presenter.viewmodel.ActViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ActFragment : Fragment() {

    private var _binding: FragmentActBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ActViewModel by viewModels()
    private val args: ActFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentActBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loadActText(args.url)

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.actText.collect { actText ->
                actText.forEach { textString ->
                    binding.actText.append("$textString\n\n")
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.isLoading.collect { binding.progress.isVisible = it }
        }

        binding.buttonShare.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT, binding.actText.text)
            intent.type = "text/plain"
            startActivity(Intent.createChooser(intent, "Send to:"))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}