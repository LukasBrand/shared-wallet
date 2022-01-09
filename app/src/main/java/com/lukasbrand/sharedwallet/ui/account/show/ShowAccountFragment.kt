package com.lukasbrand.sharedwallet.ui.account.show

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.lukasbrand.sharedwallet.R
import com.lukasbrand.sharedwallet.databinding.ShowAccountFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShowAccountFragment : Fragment() {

    private val viewModel: ShowAccountViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: ShowAccountFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.show_account_fragment, container, false)


        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            showAccountViewModel = viewModel
        }

        return binding.root
    }
}