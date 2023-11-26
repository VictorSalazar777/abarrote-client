package com.manuelsoft.domain_usecases.usecases

import kotlinx.coroutines.flow.SharedFlow

interface UpdaterUseCase {

    val resultFlow: SharedFlow<Result<Unit>>

    suspend fun update()
}