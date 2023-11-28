package ru.reaperoq.test_task.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.reaperoq.test_task.R
import ru.reaperoq.test_task.databinding.FragmentLoginBinding
import ru.reaperoq.test_task.datasource.MainDataSource
import ru.reaperoq.test_task.presentation.login.LoginViewModel
import ru.reaperoq.test_task.presentation.login.models.LoginEvent

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = LoginFragment()
    }

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        setupObservers()
        setupWidgets()
        setupListeners()
        return binding.root
    }

    private fun setupObservers() {
        viewModel.observe(viewLifecycleOwner) {
            if (it.isLoginError) {
                binding.loginLayout.helperText = resources.getString(R.string.login_empty)
            } else {
                binding.loginLayout.helperText = null
            }
            if (it.isPasswordError) {
                binding.passwordLayout.helperText = resources.getString(R.string.password_empty)
            } else {
                binding.passwordLayout.helperText = null
            }
            if (it.isLoading) {
                binding.progressBar.visibility = View.VISIBLE
                binding.login.isEnabled = false
                binding.password.isEnabled = false
                binding.loginButton.isEnabled = false
            } else {
                binding.progressBar.visibility = View.GONE
                binding.login.isEnabled = true
                binding.password.isEnabled = true
                binding.loginButton.isEnabled = true
            }
            if (it.isLoginSuccess) {
                view?.findNavController()?.navigate(R.id.action_loginFragment_to_mainFragment)
            }
            if (it.error != null) {
                binding.errorText.visibility = View.VISIBLE
                val res = when (it.error) {
                    MainDataSource.ErrorCodes.ERROR_CODE_1003 -> R.string.error_code_1003
                    MainDataSource.ErrorCodes.ERROR_CODE_NO_INTERNET -> R.string.error_code_no_internet
                    else -> R.string.error_code_other
                }
                binding.errorText.text = resources.getString(res)
            } else {
                binding.errorText.visibility = View.GONE
            }
        }
    }

    private fun setupWidgets() {
        binding.password.imeOptions = android.view.inputmethod.EditorInfo.IME_ACTION_GO
    }

    private fun setupListeners() {
        binding.loginButton.setOnClickListener {
            viewModel.obtainEvent(
                LoginEvent.ClickLogin(
                    binding.login.text.toString(),
                    binding.password.text.toString()
                )
            )
        }
        binding.password.setOnKeyListener { _, _, keyEvent ->
            if (keyEvent.keyCode == android.view.KeyEvent.KEYCODE_ENTER) {
                viewModel.obtainEvent(
                    LoginEvent.ClickLogin(
                        binding.login.text.toString(),
                        binding.password.text.toString()
                    )
                )
                true
            } else {
                false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}