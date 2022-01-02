package com.lukasbrand.sharedwallet.ui.wallet.show

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.lukasbrand.sharedwallet.R
import com.lukasbrand.sharedwallet.databinding.ShowExpenseFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShowExpenseFragment : Fragment() {

    private val viewModel: ShowExpenseViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: ShowExpenseFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.show_expense_fragment, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.showExpenseViewModel = viewModel

        return binding.root
    }

}