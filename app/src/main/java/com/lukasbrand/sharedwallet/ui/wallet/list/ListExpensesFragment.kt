package com.lukasbrand.sharedwallet.ui.wallet.list

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.lukasbrand.sharedwallet.R
import com.lukasbrand.sharedwallet.databinding.ListExpensesFragmentBinding
import com.lukasbrand.sharedwallet.datasource.ExpensesRemoteDataSource
import com.lukasbrand.sharedwallet.datasource.UsersRemoteDataSource
import com.lukasbrand.sharedwallet.datasource.firestore.FirestoreApi
import com.lukasbrand.sharedwallet.repository.ExpensesRepository
import com.lukasbrand.sharedwallet.repository.UsersRepository
import com.lukasbrand.sharedwallet.ui.wallet.list.item.ExpenseItemListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class ListExpensesFragment : Fragment() {

    //Initialize components:
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: ListExpensesFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.list_expenses_fragment, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        setHasOptionsMenu(true)




        val firestoreApi = FirestoreApi.getInstance()
        val expensesRemoteDataSource = ExpensesRemoteDataSource(firestoreApi, Dispatchers.IO)
        val usersRemoteDataSource = UsersRemoteDataSource(firestoreApi, Dispatchers.IO)
        val expensesRepository = ExpensesRepository(expensesRemoteDataSource)
        val usersRepository = UsersRepository(usersRemoteDataSource)

        val viewModelFactory =
            ListExpensesViewModelFactory(expensesRepository, usersRepository)

        val viewModel: ListExpensesViewModel =
            ViewModelProvider(this, viewModelFactory)[ListExpensesViewModel::class.java]


        viewModel.navigateToExpenseDetail.observe(viewLifecycleOwner, { expenseId ->
            expenseId?.let {
                this.findNavController().navigate(
                    ListExpensesFragmentDirections
                        .actionListExpensesFragmentToShowExpenseFragment(expenseId)
                )
                viewModel.onExpenseItemNavigated()
            }
        })

        val adapter = ExpensesAdapter(ExpenseItemListener { expenseId: String ->
            viewModel.onExpenseItemClicked(expenseId)
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