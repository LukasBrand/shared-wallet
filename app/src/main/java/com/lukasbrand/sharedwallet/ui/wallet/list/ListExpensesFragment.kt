package com.lukasbrand.sharedwallet.ui.wallet.list

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.lukasbrand.sharedwallet.R
import com.lukasbrand.sharedwallet.databinding.ListExpensesFragmentBinding
import com.lukasbrand.sharedwallet.datasource.ExpensesRemoteDataSource
import com.lukasbrand.sharedwallet.datasource.firestore.FirestoreApi
import com.lukasbrand.sharedwallet.repository.ExpensesRepository
import com.lukasbrand.sharedwallet.ui.wallet.list.item.ExpenseItemListener
import kotlinx.coroutines.Dispatchers

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

        val application = requireActivity().application

        val firestoreApi = FirestoreApi.getInstance()
        val dataSource = ExpensesRemoteDataSource(firestoreApi, Dispatchers.IO)
        val repository = ExpensesRepository(dataSource)

        val viewModelFactory = ListExpensesViewModelFactory(repository, application)

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

        val adapter = ExpensesAdapter(ExpenseItemListener { expenseId: Long ->
            viewModel.onExpenseItemClicked(expenseId)
        })
        binding.listOfExpenses.adapter = adapter
        //adapter.submitList(it)

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