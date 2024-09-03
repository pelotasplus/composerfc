package com.grubhub.composerfc.helpers

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun <T> Fragment.observeEvents(events: Flow<T>, onEvent: (T) -> Unit) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            withContext(Dispatchers.Main.immediate) {
                events.collect {
                    onEvent(it)
                }
            }
        }
    }
}

fun <T> Fragment.observeEffects(effects: Flow<T>, onEffect: (T) -> Unit) {
    observeEvents(effects, onEffect)
}
