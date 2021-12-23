package com.lukasbrand.sharedwallet.ui.title

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.lukasbrand.sharedwallet.R
import com.lukasbrand.sharedwallet.databinding.TitleFragmentBinding

class TitleFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: TitleFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.title_fragment, container, false)

        binding.lifecycleOwner = viewLifecycleOwner

        val viewModel: TitleViewModel = ViewModelProvider(this)[TitleViewModel::class.java]

        binding.titleViewModel = viewModel

        return binding.root
    }
}