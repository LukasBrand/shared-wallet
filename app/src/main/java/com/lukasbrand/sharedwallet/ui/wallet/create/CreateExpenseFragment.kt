package com.lukasbrand.sharedwallet.ui.wallet.create

import android.app.DatePickerDialog
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
import com.lukasbrand.sharedwallet.ui.wallet.create.participant.ParticipantItemListener
import com.lukasbrand.sharedwallet.ui.wallet.list.ExpensesAdapter
import com.lukasbrand.sharedwallet.ui.wallet.list.item.ExpenseItemListener
import kotlinx.coroutines.Dispatchers
import java.time.LocalDate
import java.util.*

class CreateExpenseFragment : Fragment() {

    private lateinit var viewModel: CreateExpenseViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)

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

        binding.createExpenseCreationDate.setOnClickListener(this::clickDataPicker)
        binding.createExpenseDueDate.setOnClickListener(this::clickDataPicker)

        val adapter = ParticipantsAdapter(
            ParticipantItemListener(onPaidListener = {
                TODO()
            }, onPercentageChangedListener = {
                TODO()
            }, onParticipantRemoveListener = {
                TODO()
            })
        )
        binding.createExpenseParticipants.adapter = adapter

        viewModel.participants.observe(viewLifecycleOwner, { listOfParticipants ->
            adapter.submitList(listOfParticipants)
        })

        return binding.root
    }

    private fun clickDataPicker(view: View) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this.requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val date = LocalDate.of(selectedYear, selectedMonth, selectedDay)
                when (view.id) {
                    R.id.create_expense_creation_date -> viewModel.setCreationDate(date)
                    R.id.create_expense_due_date -> viewModel.setDueDate(date)
                }
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
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