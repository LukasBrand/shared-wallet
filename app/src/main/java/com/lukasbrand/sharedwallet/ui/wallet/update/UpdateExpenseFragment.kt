package com.lukasbrand.sharedwallet.ui.wallet.update

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.lukasbrand.sharedwallet.R
import com.lukasbrand.sharedwallet.databinding.UpdateExpenseFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpdateExpenseFragment : Fragment() {

    private val viewModel: UpdateExpenseViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: UpdateExpenseFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.update_expense_fragment, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.updateExpenseViewModel = viewModel

        return binding.root
    }

}