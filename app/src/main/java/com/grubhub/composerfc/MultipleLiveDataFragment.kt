package com.grubhub.composerfc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.grubhub.composerfc.composables.MultipleVariablesUserDetails
import com.grubhub.composerfc.helpers.LoadUserDataUseCase
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MultipleLiveDataViewModel @Inject constructor(
    loadUserDataUseCase: LoadUserDataUseCase
): ViewModel() {

    val state = State()
    val events = MutableLiveData<Event>()

    init {
        viewModelScope.launch {
            val userData = loadUserDataUseCase.execute()
            state.name.value = userData.name
            state.age.value = userData.age
        }
    }

    fun onButtonClicked() {
        events.value = Event.Dismiss
    }

    sealed class Event {
        data object Dismiss: Event()
    }

    data class State(
        val name: MutableLiveData<String> = MutableLiveData<String>(""),
        val age: MutableLiveData<Int> = MutableLiveData<Int>(0),
    )
}

@AndroidEntryPoint
class MultipleLiveDataFragment : BottomSheetDialogFragment() {

    private val viewModel by viewModels<MultipleLiveDataViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.events.observe(this) { event ->
            when (event) {
                MultipleLiveDataViewModel.Event.Dismiss -> {
                    dismiss()
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                val name by viewModel.state.name.observeAsState()
                val age by viewModel.state.age.observeAsState()
                MaterialTheme {
                    MultipleVariablesUserDetails(
                        name = name,
                        age = age,
                        onButtonClicked = {
                            viewModel.onButtonClicked()
                        }
                    )
                }
            }
        }
    }
}

