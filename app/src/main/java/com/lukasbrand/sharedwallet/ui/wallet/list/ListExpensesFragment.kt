package com.lukasbrand.sharedwallet.ui.wallet.list

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
import androidx.recyclerview.widget.DividerItemDecoration
import com.lukasbrand.sharedwallet.R
import com.lukasbrand.sharedwallet.data.Expense
import com.lukasbrand.sharedwallet.databinding.ListExpensesFragmentBinding
import com.lukasbrand.sharedwallet.exhaustive
import com.lukasbrand.sharedwallet.types.Navigator
import com.lukasbrand.sharedwallet.types.Result
import com.lukasbrand.sharedwallet.ui.wallet.list.expense.ExpenseItemListener
import com.lukasbrand.sharedwallet.ui.wallet.list.expense.ExpensesAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class ListExpensesFragment : Fragment() {

    private val viewModel: ListExpensesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        requireActivity().title = getString(R.string.list_expense_title)

        val binding: ListExpensesFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.list_expenses_fragment, container, false)

        val adapter = ExpensesAdapter(
            ExpenseItemListener { expense: Expense ->
                viewModel.onExpenseItemClicked(expense)
            }
        )

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            listOfExpenses.addItemDecoration(
                DividerItemDecoration(
                    listOfExpenses.context,
                    DividerItemDecoration.VERTICAL
                )
            )
            listOfExpenses.adapter = adapter
            listExpensesCreateExpense.setOnClickListener {
                findNavController().navigate(ListExpensesFragmentDirections.actionListExpensesFragmentToCreateExpenseFragment())
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                //This is not clear to me. If two StateFlows are collected inside a single
                //coroutine launch scope only the first will be executed. Could be a bug
                launch {
                    viewModel.userId.collect { userIdResult ->
                        when (userIdResult) {
                            is Result.Error -> {
                                Toast.makeText(
                                    requireContext(),
                                    "Could not log in: '${userIdResult.exception.message}'",
                                    Toast.LENGTH_SHORT
                                ).show()
                                findNavController().navigate(
                                    ListExpensesFragmentDirections.actionListExpensesFragmentToTitleFragment()
                                )
                            }
                            is Result.Success -> {}
                            Result.Loading -> {}
                        }.exhaustive
                    }
                }
                launch {
                    viewModel.navigateToExpenseDetail.collect { navigator ->
                        when (navigator) {
                            is Navigator.Navigate -> {
                                findNavController().navigate(
                                    ListExpensesFragmentDirections
                                        .actionListExpensesFragmentToShowExpenseFragment(navigator.data)
                                )
                                viewModel.onExpenseItemNavigated()
                            }
                            is Navigator.Error -> {
                                Toast.makeText(
                                    requireContext(),
                                    "Could not switch to Expense: '${navigator.exception.message}'",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            Navigator.Loading -> {}
                            Navigator.Stay -> {}
                        }.exhaustive
                    }
                }
                launch {
                    viewModel.navigateToTitle.collect { navigator ->
                        when (navigator) {
                            is Navigator.Navigate -> {
                                findNavController().navigate(
                                    ListExpensesFragmentDirections
                                        .actionListExpensesFragmentToTitleFragment()
                                )
                                viewModel.onTitleNavigated()
                            }
                            is Navigator.Error -> {
                                Toast.makeText(
                                    requireContext(),
                                    "Could not move to title screen: '${navigator.exception.message}'",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            Navigator.Loading -> {}
                            Navigator.Stay -> {}
                        }.exhaustive
                    }
                }
                launch {
                    viewModel.expenses.collect { listOfExpenses ->
                        adapter.submitList(listOfExpenses)
                    }
                }
            }
        }
        return binding.root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.list_expenses_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.show_account -> {
                findNavController().navigate(ListExpensesFragmentDirections.actionListExpensesFragmentToShowAccountFragment())
                true
            }
            R.id.log_out_account -> {
                viewModel.onLogOut()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}