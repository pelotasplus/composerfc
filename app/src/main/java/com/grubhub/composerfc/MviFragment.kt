package com.grubhub.composerfc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.grubhub.composerfc.composables.SingleDisplayableUserDetails
import com.grubhub.composerfc.composables.UserDetailsDisplayable
import com.grubhub.composerfc.helpers.LoadUserDataUseCase
import com.grubhub.composerfc.helpers.observeEffects
import com.grubhub.composerfc.helpers.observeEvents
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MviViewModel @Inject constructor(
    loadUserDataUseCase: LoadUserDataUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(State())
    val state = _state.asStateFlow()
    private val _effects = Channel<Effect>()
    val effects = _effects.receiveAsFlow()

    init {
        viewModelScope.launch {
            val userData = loadUserDataUseCase.execute()
            _state.update {
                it.copy(
                    displayable = UserDetailsDisplayable(
                        name = userData.name,
                        age = userData.age
                    )
                )
            }
        }
    }

    fun onEvent(event: Event) {
        when (event) {
            Event.OnButtonClicked -> handleOnButtonClicked()
        }
    }

    private fun handleOnButtonClicked() {
        viewModelScope.launch {
            _effects.send(Effect.Dismiss)
        }
    }

    sealed class Event {
        data object OnButtonClicked : Event()
    }

    sealed class Effect {
        data object Dismiss: Effect()
    }

    data class State(
        val displayable: UserDetailsDisplayable = UserDetailsDisplayable()
    )
}

@AndroidEntryPoint
class MviFragment : BottomSheetDialogFragment() {

    private val viewModel by viewModels<MviViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        observeEffects(viewModel.effects) { effect ->
            when (effect) {
                MviViewModel.Effect.Dismiss -> {
                    dismiss()
                }
            }
        }

        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                val state by viewModel.state.collectAsState()
                MaterialTheme {
                    SingleDisplayableUserDetails(
                        displayable = state.displayable,
                        onButtonClicked = {
                            viewModel.onEvent(MviViewModel.Event.OnButtonClicked)
                        }
                    )
                }
            }
        }
    }
}
