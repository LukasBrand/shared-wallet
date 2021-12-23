package com.lukasbrand.sharedwallet.ui.wallet.create

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.lukasbrand.sharedwallet.R
import com.lukasbrand.sharedwallet.databinding.CreateAccountFragmentBinding
import com.lukasbrand.sharedwallet.databinding.CreateExpenseFragmentBinding

class CreateExpenseFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: CreateExpenseFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.create_expense_fragment, container, false)

        binding.lifecycleOwner = viewLifecycleOwner

        val viewModel: CreateExpenseViewModel =
            ViewModelProvider(this)[CreateExpenseViewModel::class.java]

        binding.createExpenseViewModel = viewModel

        return binding.root
    }
}