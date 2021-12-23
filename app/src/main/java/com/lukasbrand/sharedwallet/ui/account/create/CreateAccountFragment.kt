package com.lukasbrand.sharedwallet.ui.account.create

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.lukasbrand.sharedwallet.R
import com.lukasbrand.sharedwallet.databinding.CreateAccountFragmentBinding

class CreateAccountFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: CreateAccountFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.create_account_fragment, container, false)

        binding.lifecycleOwner = viewLifecycleOwner

        val viewModel: CreateAccountViewModel =
            ViewModelProvider(this)[CreateAccountViewModel::class.java]

        binding.createAccountViewModel = viewModel

        return binding.root
    }
}