package com.lukasbrand.sharedwallet.ui.account.show

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
import com.lukasbrand.sharedwallet.databinding.ShowAccountFragmentBinding
import com.lukasbrand.sharedwallet.exhaustive
import com.lukasbrand.sharedwallet.types.Navigator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ShowAccountFragment : Fragment() {

    private val viewModel: ShowAccountViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        requireActivity().title = getString(R.string.show_account_title)

        val binding: ShowAccountFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.show_account_fragment, container, false)

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            showAccountViewModel = viewModel
            showAccountEdit.setOnClickListener {
                viewModel.onAccountEdit()
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                //This is not clear to me. If two StateFlows are collected inside a single
                //coroutine launch scope only the first will be executed. Could be a bug
                launch {

                    viewModel.navigateToEditAccount.collect { navigator ->
                        when (navigator) {
                            is Navigator.Navigate -> {
                                findNavController().navigate(
                                    ShowAccountFragmentDirections.actionShowAccountFragmentToUpdateAccountFragment(
                                        viewModel.user.value
                                    )
                                )
                                viewModel.onAccountShownNavigated()
                            }
                            is Navigator.Error -> {
                                Toast.makeText(
                                    requireContext(),
                                    "Could not switch to Edit Account: '${navigator.exception.message}'",
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


    //Options menu integration:
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.show_account_menu, menu)
    }

    //TODO: bind actions to menu elements
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.show_account_menu_delete_account -> {
                findNavController().navigate(ShowAccountFragmentDirections.actionShowAccountFragmentToDeleteAccountFragment())
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}