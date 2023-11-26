package com.manuelsoft.domain_repository.repository

interface RepositoryUpdater {

    suspend fun update(): Result<Unit>

}