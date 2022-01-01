package com.lukasbrand.sharedwallet.ui.account.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.type.Money
import com.lukasbrand.sharedwallet.R
import com.lukasbrand.sharedwallet.databinding.CreateAccountFragmentBinding
import com.lukasbrand.sharedwallet.datasource.AuthenticationRemoteDataSource
import com.lukasbrand.sharedwallet.datasource.firestore.FirebaseAuthApi
import com.lukasbrand.sharedwallet.repository.AuthenticationRepository
import kotlinx.coroutines.Dispatchers

class CreateAccountFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: CreateAccountFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.create_account_fragment, container, false)

        binding.lifecycleOwner = viewLifecycleOwner

        val firebaseAuthApi = FirebaseAuthApi.getInstance()
        val authenticationRemoteDataSource =
            AuthenticationRemoteDataSource(firebaseAuthApi, Dispatchers.IO)
        val authenticationRepository = AuthenticationRepository(authenticationRemoteDataSource)

        val viewModelFactory = CreateAccountViewModelFactory(authenticationRepository)

        val viewModel: CreateAccountViewModel =
            ViewModelProvider(this, viewModelFactory)[CreateAccountViewModel::class.java]

        binding.createAccountViewModel = viewModel

        viewModel.onAccountCreated.observe(viewLifecycleOwner) {
            this.findNavController().navigate(
                CreateAccountFragmentDirections.actionCreateAccountFragmentToListExpensesFragment()
            )
        }

        return binding.root
    }
}