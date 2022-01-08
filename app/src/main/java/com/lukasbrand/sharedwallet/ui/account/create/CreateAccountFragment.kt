package com.lukasbrand.sharedwallet.ui.account.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.lukasbrand.sharedwallet.R
import com.lukasbrand.sharedwallet.databinding.CreateAccountFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateAccountFragment : Fragment() {

    private val viewModel: CreateAccountViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: CreateAccountFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.create_account_fragment, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.createAccountViewModel = viewModel

        viewModel.onAccountCreated.observe(viewLifecycleOwner) {
            this.findNavController().navigate(
                CreateAccountFragmentDirections.actionCreateAccountFragmentToListExpensesFragment()
            )
        }
        return binding.root
    }
}