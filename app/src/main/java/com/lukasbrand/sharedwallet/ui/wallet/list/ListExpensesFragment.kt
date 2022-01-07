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
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.lukasbrand.sharedwallet.R
import com.lukasbrand.sharedwallet.databinding.ListExpensesFragmentBinding
import com.lukasbrand.sharedwallet.exhaustive
import com.lukasbrand.sharedwallet.types.Navigator
import com.lukasbrand.sharedwallet.ui.wallet.list.item.ExpenseItemListener
import com.lukasbrand.sharedwallet.ui.wallet.list.item.ExpensesAdapter
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

        val adapter = ExpensesAdapter(ExpenseItemListener { expenseId: String ->
            viewModel.onExpenseItemClicked(expenseId)
        })

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            listOfExpenses.adapter = adapter
            listExpensesCreateExpense.setOnClickListener {
                findNavController().navigate(ListExpensesFragmentDirections.actionListExpensesFragmentToCreateExpenseFragment())
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    viewModel.userId.collect { userId ->
                        if (userId == null) {
                            findNavController().navigate(
                                ListExpensesFragmentDirections.actionListExpensesFragmentToTitleFragment()
                            )
                        }
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
                                println(navigator.exception.message)
                                println(navigator.exception.stackTrace.toString())
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


    //Options menu integration:
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.list_expenses_menu, menu)
    }

    //TODO: bind actions to menu elements
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(
            item,
            requireView().findNavController()
        ) || super.onOptionsItemSelected(item)
    }
}