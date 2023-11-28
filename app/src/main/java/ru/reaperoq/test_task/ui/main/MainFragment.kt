package ru.reaperoq.test_task.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import ru.reaperoq.test_task.R
import ru.reaperoq.test_task.databinding.FragmentMainBinding
import ru.reaperoq.test_task.datasource.MainDataSource
import ru.reaperoq.test_task.presentation.main.MainViewModel
import ru.reaperoq.test_task.presentation.main.models.MainEvent

@AndroidEntryPoint
class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val adapter = PaymentsRecyclerViewAdapter()
    private val viewModel: MainViewModel by viewModels()

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        setupWidgets()
        setupObservers()
        setupListeners()
        viewModel.obtainEvent(MainEvent.GetPayments)
        return binding.root
    }

    private fun setupObservers() {
        viewModel.observe(viewLifecycleOwner) {
            adapter.update(it.payments)
            if (it.logout) {
                binding.root.findNavController().navigate(
                    R.id.action_mainFragment_to_loginFragment
                )
            }
            if (it.error == MainDataSource.ErrorCodes.ERROR_CODE_NO_INTERNET) {
                binding.noInternetConnection.root.visibility = View.VISIBLE
            } else {
                binding.noInternetConnection.root.visibility = View.GONE
            }
            binding.swipeRefreshLayout.isRefreshing = it.isLoading
        }
    }

    private fun setupListeners() {
        binding.toolbar.menu.findItem(R.id.action_logout).setOnMenuItemClickListener {
            showLogoutDialog()
            true
        }
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.obtainEvent(MainEvent.GetPayments)
        }
    }

    private fun showLogoutDialog() =
        MaterialAlertDialogBuilder(requireContext())
            .setIcon(R.drawable.rounded_logout_24)
            .setTitle(R.string.logout_confirm)
            .setMessage(R.string.logout_description)
            .setPositiveButton(R.string.yes) { _, _ ->
                viewModel.obtainEvent(MainEvent.Logout)
            }
            .setNegativeButton(R.string.no) { _, _ -> }
            .show()

    private fun setupWidgets() {
        binding.paymentsList.adapter = adapter
        binding.paymentsList.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}