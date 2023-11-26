package com.manuelsoft.domain_impl.usecase

import com.manuelsoft.domain_repository.repository.RepositoryUpdater
import com.manuelsoft.domain_usecases.usecases.UpdaterUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class UpdaterUseCaseImpl(val updater: RepositoryUpdater) : UpdaterUseCase {

    private val _resultFlow = MutableSharedFlow<Result<Unit>>()
    override val resultFlow: SharedFlow<Result<Unit>> = _resultFlow.asSharedFlow()

    override suspend fun update() {
        val result = updater.update()
        _resultFlow.emit(result)
    }

}