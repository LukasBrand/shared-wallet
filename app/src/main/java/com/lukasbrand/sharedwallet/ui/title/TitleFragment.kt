package com.lukasbrand.sharedwallet.ui.title

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.lukasbrand.sharedwallet.R
import com.lukasbrand.sharedwallet.databinding.TitleFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TitleFragment : Fragment() {

    private val viewModel: TitleViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: TitleFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.title_fragment, container, false)

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            titleViewModel = viewModel
            emailRegister.setOnClickListener {
                findNavController().navigate(
                    TitleFragmentDirections.actionTitleFragmentToCreateAccountFragment()
                )
            }
            emailLogin.setOnClickListener {
                findNavController().navigate(
                    TitleFragmentDirections.actionTitleFragmentToLoginFragment()
                )
            }

            googleLogin.setOnClickListener {
                //Open Google Intent
            }
            githubLogin.setOnClickListener {
                //Open Github Intent
            }

        }
        return binding.root
    }
}