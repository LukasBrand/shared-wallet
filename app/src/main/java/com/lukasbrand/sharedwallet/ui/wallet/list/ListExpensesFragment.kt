package com.lukasbrand.sharedwallet.ui.wallet.list

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.lukasbrand.sharedwallet.R
import com.lukasbrand.sharedwallet.databinding.ListExpensesFragmentBinding
import com.lukasbrand.sharedwallet.ui.wallet.list.item.ExpenseItemListener
import com.lukasbrand.sharedwallet.ui.wallet.list.item.ExpensesAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class ListExpensesFragment : Fragment() {

    private val viewModel: ListExpensesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)

        val binding: ListExpensesFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.list_expenses_fragment, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        val adapter = ExpensesAdapter(ExpenseItemListener { expenseId: String ->
            viewModel.onExpenseItemClicked(expenseId)
        })

        viewModel.navigateToExpenseDetail.observe(viewLifecycleOwner, { expenseId ->
            expenseId?.let {
                this.findNavController().navigate(
                    ListExpensesFragmentDirections
                        .actionListExpensesFragmentToShowExpenseFragment(expenseId)
                )
                viewModel.onExpenseItemNavigated()
            }
        })
        binding.listOfExpenses.adapter = adapter

        viewModel.expenses.observe(viewLifecycleOwner, { listOfExpenses ->
            adapter.submitList(listOfExpenses)
        })

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