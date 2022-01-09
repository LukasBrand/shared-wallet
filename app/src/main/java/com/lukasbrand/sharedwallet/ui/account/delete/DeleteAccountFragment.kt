package com.lukasbrand.sharedwallet.ui.account.delete

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.lukasbrand.sharedwallet.R
import com.lukasbrand.sharedwallet.databinding.DeleteAccountFragmentBinding
import com.lukasbrand.sharedwallet.exhaustive
import com.lukasbrand.sharedwallet.types.Navigator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DeleteAccountFragment : Fragment() {

    private val viewModel: DeleteAccountViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        requireActivity().title = getString(R.string.delete_account_title)

        val binding: DeleteAccountFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.delete_account_fragment, container, false)

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            deleteAccountViewModel = viewModel
            deleteAccountDeleteAction.setOnClickListener { viewModel.deleteAccount() }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                //This is not clear to me. If two StateFlows are collected inside a single
                //coroutine launch scope only the first will be executed. Could be a bug
                launch {
                    viewModel.navigateToTitle.collect { navigator ->
                        when (navigator) {
                            is Navigator.Navigate -> {
                                findNavController().navigate(DeleteAccountFragmentDirections.actionDeleteAccountFragmentToTitleFragment())
                                viewModel.onAccountDeleteNavigated()
                            }
                            is Navigator.Error -> {
                                Toast.makeText(
                                    requireContext(),
                                    "Could not switch to Title: '${navigator.exception.message}'",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            Navigator.Loading -> {}
                            Navigator.Stay -> {}
                        }.exhaustive
                    }
                }
            }
        }
        return binding.root
    }
}
