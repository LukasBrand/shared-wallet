package com.lukasbrand.sharedwallet.ui.wallet.create

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.lukasbrand.sharedwallet.R
import com.lukasbrand.sharedwallet.data.User
import com.lukasbrand.sharedwallet.databinding.CreateExpenseFragmentBinding
import com.lukasbrand.sharedwallet.ui.wallet.create.participant.ParticipantItemListener
import com.lukasbrand.sharedwallet.ui.wallet.create.participant.ParticipantsAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.util.*

@AndroidEntryPoint
class CreateExpenseFragment : Fragment() {

    private val viewModel : CreateExpenseViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)

        val binding: CreateExpenseFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.create_expense_fragment, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
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

        viewModel.email.observe(viewLifecycleOwner, viewModel::searchForUser)

        binding.createExpenseAddParticipants.setOnClickListener {
            viewModel.addParticipant(
                User(
                    viewModel.user.id,
                    viewModel.user.name,
                    viewModel.user.email,
                    viewModel.user.image
                )
            )
            TODO("Bad Code. Leave model data classes in data layer")
        }

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