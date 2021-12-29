package com.lukasbrand.sharedwallet.ui.wallet.create

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.lukasbrand.sharedwallet.R
import com.lukasbrand.sharedwallet.databinding.CreateExpenseFragmentBinding
import com.lukasbrand.sharedwallet.datasource.ExpensesRemoteDataSource
import com.lukasbrand.sharedwallet.datasource.UsersRemoteDataSource
import com.lukasbrand.sharedwallet.datasource.firestore.FirestoreApi
import com.lukasbrand.sharedwallet.repository.ExpensesRepository
import com.lukasbrand.sharedwallet.repository.UsersRepository
import kotlinx.coroutines.Dispatchers

class CreateExpenseFragment : Fragment() {

    private lateinit var viewModel: CreateExpenseViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: CreateExpenseFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.create_expense_fragment, container, false)

        binding.lifecycleOwner = viewLifecycleOwner

        val firestoreApi = FirestoreApi.getInstance()
        val expensesRemoteDataSource = ExpensesRemoteDataSource(firestoreApi, Dispatchers.IO)
        val usersRemoteDataSource = UsersRemoteDataSource(firestoreApi, Dispatchers.IO)
        val expensesRepository = ExpensesRepository(expensesRemoteDataSource)
        val usersRepository = UsersRepository(usersRemoteDataSource)

        val viewModelFactory = CreateExpenseViewModelFactory(expensesRepository, usersRepository)

        viewModel = ViewModelProvider(this, viewModelFactory)[CreateExpenseViewModel::class.java]

        binding.createExpenseViewModel = viewModel

        return binding.root
    }


    //Options menu integration:
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.create_expense_menu, menu)
    }

    //TODO: bind actions to menu elements
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.create_expense -> {
                viewModel.createExpense()
                this.findNavController()
                    .navigate(CreateExpenseFragmentDirections.actionCreateExpenseFragmentToListExpensesFragment())
                true
            }
            R.id.abort_create_expense -> {
                this.findNavController()
                    .navigate(CreateExpenseFragmentDirections.actionCreateExpenseFragmentToListExpensesFragment())
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}