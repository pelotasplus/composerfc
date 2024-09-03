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
class SingleLiveDataViewModel @Inject constructor(
    loadUserDataUseCase: LoadUserDataUseCase
): ViewModel() {

    val state = MutableLiveData(State())
    val events = MutableLiveData<Event>()

    init {
        viewModelScope.launch {
            val userData = loadUserDataUseCase.execute()
            state.value = requireNotNull(state.value).copy(
                name = userData.name,
                age = userData.age
            )
        }
    }

    fun onButtonClicked() {
        events.value = Event.Dismiss
    }

    sealed class Event {
        data object Dismiss: Event()
    }

    data class State(
        val name: String = "",
        val age: Int = 0
    )
}

@AndroidEntryPoint
class SingleLiveDataFragment : BottomSheetDialogFragment() {

    private val viewModel by viewModels<SingleLiveDataViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.events.observe(this) { event ->
            when (event) {
                SingleLiveDataViewModel.Event.Dismiss -> {
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
                val state by viewModel.state.observeAsState()
                MaterialTheme {
                    MultipleVariablesUserDetails(
                        name = state?.name,
                        age = state?.age,
                        onButtonClicked = {
                            viewModel.onButtonClicked()
                        }
                    )
                }
            }
        }
    }
}
