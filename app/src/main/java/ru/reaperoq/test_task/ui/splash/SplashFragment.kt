package ru.reaperoq.test_task.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.reaperoq.test_task.R
import ru.reaperoq.test_task.databinding.FragmentSplashBinding
import ru.reaperoq.test_task.presentation.splash.SplashViewModel
import ru.reaperoq.test_task.presentation.splash.models.SplashEvent

@AndroidEntryPoint
class SplashFragment : Fragment() {
    companion object {
        fun newInstance() = SplashFragment()
    }

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SplashViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        setupObservers()
        viewModel.obtainEvent(SplashEvent.CheckAuth)
        return binding.root
    }

    private fun setupObservers() {
        viewModel.observe(viewLifecycleOwner) {
            if (it.isLoggedIn == true) {
                binding.root.findNavController().navigate(
                    R.id.action_splashFragment_to_mainFragment
                )
            } else if (it.isLoggedIn == false) {
                binding.root.findNavController().navigate(
                    R.id.action_splashFragment_to_loginFragment
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}