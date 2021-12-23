package com.lukasbrand.sharedwallet.ui.wallet.update

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.lukasbrand.sharedwallet.R
import com.lukasbrand.sharedwallet.databinding.UpdateExpenseFragmentBinding

class UpdateExpenseFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: UpdateExpenseFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.update_expense_fragment, container, false)

        binding.lifecycleOwner = viewLifecycleOwner

        val viewModel: UpdateExpenseViewModel =
            ViewModelProvider(this)[UpdateExpenseViewModel::class.java]

        binding.updateExpenseViewModel = viewModel

        return binding.root
    }

}