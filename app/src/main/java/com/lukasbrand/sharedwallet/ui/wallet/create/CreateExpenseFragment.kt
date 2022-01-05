package com.lukasbrand.sharedwallet.ui.wallet.create

import android.app.Activity
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.type.LatLng
import com.lukasbrand.sharedwallet.R
import com.lukasbrand.sharedwallet.databinding.CreateExpenseFragmentBinding
import com.lukasbrand.sharedwallet.ui.wallet.create.participant.ParticipantItemListener
import com.lukasbrand.sharedwallet.ui.wallet.create.participant.ParticipantsAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.util.*
import com.google.android.gms.maps.model.LatLng as MapsLatLng

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
                viewModel.participantHasPaid(participantId, isPaid)
            }, onPercentageChangedListener = { participantId, percent ->
                viewModel.participantPercentage(participantId, percent)
            }, onParticipantRemoveListener = { participantId ->
                viewModel.removeParticipant(participantId)
            })
        )

        viewModel.apply {
            participants.observe(viewLifecycleOwner, { listOfParticipants ->
                adapter.submitList(listOfParticipants)
            })
            viewModel.email.observe(viewLifecycleOwner, viewModel::searchForUser)
        }

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            createExpenseViewModel = viewModel
            createExpenseParticipants.adapter = adapter
            createExpenseCreationDate.setOnClickListener(this@CreateExpenseFragment::clickDataPicker)
            createExpenseDueDate.setOnClickListener(this@CreateExpenseFragment::clickDataPicker)

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
                val date = LocalDate.of(selectedYear, selectedMonth + 1, selectedDay)
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
                findNavController()
                    .navigate(CreateExpenseFragmentDirections.actionCreateExpenseFragmentToListExpensesFragment())
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