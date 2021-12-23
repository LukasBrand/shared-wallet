package com.lukasbrand.sharedwallet.ui.account.update

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.lukasbrand.sharedwallet.R
import com.lukasbrand.sharedwallet.databinding.UpdateAccountFragmentBinding

class UpdateAccountFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: UpdateAccountFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.update_account_fragment, container, false)

        binding.lifecycleOwner = viewLifecycleOwner

        val viewModel: UpdateAccountViewModel =
            ViewModelProvider(this)[UpdateAccountViewModel::class.java]

        binding.updateAccountViewModel = viewModel

        return binding.root
    }
}