package com.lukasbrand.sharedwallet.ui.account.show

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.lukasbrand.sharedwallet.R
import com.lukasbrand.sharedwallet.databinding.ShowAccountFragmentBinding

class ShowAccountFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: ShowAccountFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.show_account_fragment, container, false)

        binding.lifecycleOwner = viewLifecycleOwner

        val viewModel: ShowAccountViewModel =
            ViewModelProvider(this)[ShowAccountViewModel::class.java]

        binding.showAccountViewModel = viewModel

        return binding.root
    }
}