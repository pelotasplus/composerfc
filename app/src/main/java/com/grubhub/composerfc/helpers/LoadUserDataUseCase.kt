package com.grubhub.composerfc.helpers

import kotlinx.coroutines.delay
import javax.inject.Inject

class LoadUserDataUseCase @Inject constructor() {

    suspend fun execute(): UserData {
        delay(5000)
        return UserData(
            "John Smith",
            46
        )
    }

    data class UserData(
        val name: String,
        val age: Int
    )
}