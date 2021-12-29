package com.lukasbrand.sharedwallet.ui.title

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.credentials.CredentialsClient
import com.lukasbrand.sharedwallet.R
import com.lukasbrand.sharedwallet.databinding.TitleFragmentBinding
import com.lukasbrand.sharedwallet.ui.wallet.list.ListExpensesFragmentDirections

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

        binding.emailRegister.setOnClickListener {
            this.findNavController().navigate(
                TitleFragmentDirections.actionTitleFragmentToCreateAccountFragment()
            )
        }

        binding.emailLogin.setOnClickListener {
            this.findNavController().navigate(
                TitleFragmentDirections.actionTitleFragmentToLoginFragment()
            )
        }

        binding.googleLogin.setOnClickListener {
            //Open Google Intent
        }

        binding.githubLogin.setOnClickListener {
            //Open Github Intent
        }

        return binding.root
    }
}