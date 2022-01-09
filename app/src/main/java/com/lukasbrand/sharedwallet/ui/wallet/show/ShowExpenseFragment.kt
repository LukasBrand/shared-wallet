package com.lukasbrand.sharedwallet.ui.wallet.show

import android.content.Intent
import android.net.Uri
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
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.lukasbrand.sharedwallet.R
import com.lukasbrand.sharedwallet.databinding.ShowExpenseFragmentBinding
import com.lukasbrand.sharedwallet.exhaustive
import com.lukasbrand.sharedwallet.types.Navigator
import com.lukasbrand.sharedwallet.ui.wallet.show.participant.ShowParticipantsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@ExperimentalCoroutinesApi
@AndroidEntryPoint
class ShowExpenseFragment : Fragment() {

    private val viewModel: ShowExpenseViewModel by viewModels()

    private lateinit var mapView: MapView;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        requireActivity().title = getString(R.string.show_expense_title)

        val binding: ShowExpenseFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.show_expense_fragment, container, false)

        val adapter = ShowParticipantsAdapter()

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            showExpenseViewModel = viewModel
            mapView = showExpenseLocation
            showExpenseParticipants.adapter = adapter
        }
        mapView.onCreate(savedInstanceState)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                //This is not clear to me. If two StateFlows are collected inside a single
                //coroutine launch scope only the first will be executed. Could be a bug
                launch {
                    viewModel.location.collect { location ->
                        mapView.getMapAsync { map ->
                            map.uiSettings.isMyLocationButtonEnabled = false
                            map.uiSettings.setAllGesturesEnabled(false)
                            map.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    LatLng(
                                        location.latitude,
                                        location.longitude
                                    ), 18F
                                )
                            )
                            map.setOnMapClickListener {
                                val gmmIntentUri =
                                    Uri.parse("geo:" + location.latitude + "," + location.longitude + "?z=18")
                                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                                mapIntent.setPackage("com.google.android.apps.maps")
                                startActivity(mapIntent)
                            }
                        }
                    }
                }

                launch {
                    viewModel.participants.collect { participants ->
                        adapter.submitList(participants)
                    }
                }

                launch {
                    viewModel.navigateToExpenseUpdate.collect { navigator ->
                        when (navigator) {
                            is Navigator.Navigate -> {
                                findNavController().navigate(
                                    ShowExpenseFragmentDirections.actionShowExpenseFragmentToUpdateExpenseFragment(
                                        navigator.data
                                    )
                                )
                                viewModel.onExpenseUpdateNavigated()
                            }
                            is Navigator.Error -> {
                                Toast.makeText(
                                    requireContext(),
                                    "Could not switch to Edit Expense: '${navigator.exception.message}'",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            Navigator.Loading -> {}
                            Navigator.Stay -> {}
                        }.exhaustive
                    }
                }

                launch {
                    viewModel.navigateToListExpenses.collect { navigator ->
                        when (navigator) {
                            is Navigator.Navigate -> {
                                findNavController().navigate(
                                    ShowExpenseFragmentDirections.actionShowExpenseFragmentToListExpensesFragment()
                                )
                                viewModel.onListExpensesNavigated()
                            }
                            is Navigator.Error -> {
                                Toast.makeText(
                                    requireContext(),
                                    "Could not switch to List Expenses: '${navigator.exception.message}'",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            Navigator.Loading -> {}
                            Navigator.Stay -> {}
                        }.exhaustive
                    }
                }
            }
        }

        return binding.root
    }


    override fun onStart() {
        mapView.onStart()
        super.onStart()
    }

    override fun onResume() {
        mapView.onResume()
        super.onResume()
    }

    override fun onPause() {
        mapView.onPause()
        super.onPause()
    }

    override fun onStop() {
        mapView.onStop()
        super.onStop()
    }

    override fun onDestroy() {
        mapView.onDestroy()
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        mapView.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        mapView.onLowMemory()
        super.onLowMemory()
    }


    //Options menu integration:
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.show_expense_menu, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                //This is not clear to me. If two StateFlows are collected inside a single
                //coroutine launch scope only the first will be executed. Could be a bug
                launch {
                    viewModel.isOwner.collect {
                        //currently always false as this feature is not created
                        menu.findItem(R.id.show_expense_menu_edit_expense).isEnabled = false
                        menu.findItem(R.id.show_expense_menu_remove_expense).isEnabled = it
                    }
                }
            }
        }

    }

    //TODO: bind actions to menu elements
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.show_expense_menu_edit_expense -> {
                viewModel.onExpenseUpdateClicked()
                true
            }
            R.id.show_expense_menu_remove_expense -> {
                viewModel.onExpenseRemoveClicked()
                true
            }
            R.id.show_expense_menu_paid_expense -> {
                viewModel.onExpensePaidClicked()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}