package com.lukasbrand.sharedwallet.ui.wallet.create

import android.app.Activity
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.text.isDigitsOnly
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.type.LatLng
import com.lukasbrand.sharedwallet.R
import com.lukasbrand.sharedwallet.data.Navigator
import com.lukasbrand.sharedwallet.data.Result
import com.lukasbrand.sharedwallet.databinding.CreateExpenseFragmentBinding
import com.lukasbrand.sharedwallet.exhaustive
import com.lukasbrand.sharedwallet.ui.wallet.create.participant.ParticipantItemListener
import com.lukasbrand.sharedwallet.ui.wallet.create.participant.ParticipantsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*
import com.google.android.gms.maps.model.LatLng as MapsLatLng

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class CreateExpenseFragment : Fragment() {

    private val viewModel: CreateExpenseViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        requireActivity().title = getString(R.string.create_expense_title)

        val binding: CreateExpenseFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.create_expense_fragment, container, false)

        val adapter = ParticipantsAdapter(
            ParticipantItemListener(onPaidListener = { participantId, isPaid ->
                viewModel.setParticipantHasPaid(participantId, isPaid)
            }, onPercentageChangedListener = { participantId, percent ->
                viewModel.setParticipantPercentage(participantId, percent)
            }, onParticipantRemoveListener = { participantId ->
                viewModel.removeParticipant(participantId)
            })
        )

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.expense.collect { expenseResult ->
                    when (expenseResult) {
                        is Result.Success -> {
                            adapter.submitList(expenseResult.data.participants)
                            println("updated expense")
                        }
                        is Result.Error -> {}
                        Result.Loading -> {}
                    }.exhaustive
                }
                viewModel.createExpenseCompleted.collect { navigator ->
                    when (navigator) {
                        is Navigator.Navigate -> {
                            findNavController()
                                .navigate(CreateExpenseFragmentDirections.actionCreateExpenseFragmentToListExpensesFragment())
                            viewModel.onNavigatedAfterExpenseCompleted()
                        }
                        is Navigator.Error -> {}
                        Navigator.Loading -> {}
                        Navigator.Stay -> {}
                    }.exhaustive
                }
            }
        }

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            createExpenseViewModel = viewModel
            createExpenseParticipants.adapter = adapter

            createExpenseName.doOnTextChanged { text, _, _, _ ->
                text?.let {
                    viewModel.setName(it.toString())
                }
            }
            createExpenseCreationDate.setOnClickListener(this@CreateExpenseFragment::clickDataPicker)
            createExpenseDueDate.setOnClickListener(this@CreateExpenseFragment::clickDataPicker)
            createExpensePriceSmall.setOnClickListener {
                viewModel.setExpenseAmount(BigDecimal("10.00"))
            }
            createExpensePriceMiddle.setOnClickListener {
                viewModel.setExpenseAmount(BigDecimal("20.00"))
            }
            createExpensePriceHigh.setOnClickListener {
                viewModel.setExpenseAmount(BigDecimal("50.00"))
            }
            createExpensePriceCustom.doOnTextChanged { text, _, _, _ ->
               if (text != null && text.isNotEmpty() && text.isDigitsOnly()) {
                   viewModel.setExpenseAmount(BigDecimal(text.toString()))
               }
            }
            createExpensePotentialParticipantEmail.doOnTextChanged { text, _, _, _ ->
                text?.let {
                    viewModel.setParticipantEmail(it.toString())
                }
            }
            createExpenseAddParticipants.setOnClickListener {
                viewModel.addParticipant()
            }
            createExpenseLocation.setOnClickListener {
                // Set the fields to specify which types of place data to return after the user has made a selection.
                val fields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)

                val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                    .setInitialQuery(viewModel.locationQuery.value)
                    .build(requireContext())

                startForResult.launch(intent)
            }
        }
        return binding.root
    }

    private fun clickDataPicker(view: View) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val date = LocalDate.of(selectedYear, selectedMonth + 1, selectedDay).atStartOfDay()
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

    //Location activity registration
    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when (result.resultCode) {
                Activity.RESULT_OK -> {
                    result.data?.let { intent ->
                        val place = Autocomplete.getPlaceFromIntent(intent)
                        place.name?.let { name ->
                            viewModel.setLocationQuery(name)
                        }
                        place.latLng?.let { mapsLatLng: MapsLatLng ->
                            val latLng = LatLng.newBuilder()
                                .setLatitude(mapsLatLng.latitude)
                                .setLongitude(mapsLatLng.longitude)
                                .build()
                            viewModel.setLocation(latLng)
                        }

                    }
                }
                AutocompleteActivity.RESULT_ERROR -> {
                    result.data?.let {
                        val status = Autocomplete.getStatusFromIntent(it)
                        Toast.makeText(
                            requireContext(),
                            "Failed to get place '${status.statusMessage}'",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                Activity.RESULT_CANCELED -> {
                    // The user canceled the operation.
                }
            }

        }


    //Options menu integration:
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.create_expense_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.create_expense -> {
                viewModel.createExpense()
                true
            }
            R.id.cancel_create_expense -> {
                findNavController()
                    .navigate(CreateExpenseFragmentDirections.actionCreateExpenseFragmentToListExpensesFragment())
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}