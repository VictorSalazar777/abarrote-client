package com.manuelsoft.repository

import com.manuelsoft.repository.impl.RepositoryImpl
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test


class RepositoryTest {

    private lateinit var repository: Repository

    @Before
    fun setup() {

        val fakeProductEntityDao = FakeProductEntityDao(false)
        repository = RepositoryImpl(fakeProductEntityDao)
    }



    @Test
    fun getSize_insertProducts_returnsProductEntitiesSize() = runBlocking {
        val prod = Product(0, "aceite", 6.0f)
        val prod2 = Product(0, "arroz", 7.0f)
        val prod3 = Product(0, "lejia", 4.0f)
        val prod4 = Product(0, "leche", 5.0f)

        repository.add(prod)
        repository.add(prod2)
        repository.add(prod3)
        repository.add(prod4)

        assertTrue(repository.getSize() == 4)

    }

    @Test
    fun getAll_insertProducts_returnsSameProductWithRightId() = runBlocking {
        val prod = Product(0, "aceite", 6.0f)
        val prod2 = Product(0, "arroz", 7.0f)
        val prod3 = Product(0, "lejia", 4.0f)
        val prod4 = Product(0, "leche", 5.0f)
        val tester = listOf(prod, prod2, prod3, prod4)

        repository.add(prod)
        repository.add(prod2)
        repository.add(prod3)
        repository.add(prod4)

        val prods = repository.getAll()

        assertTrue(prods[0].id == 1)
        assertTrue(prods[1].id == 2)
        assertTrue(prods[2].id == 3)
        assertTrue(prods[3].id == 4)

        for(i in 0 .. 3) {
            assertTrue(prods[i].name == tester[i].name)
            assertTrue(prods[i].price == tester[i].price)
        }
    }

    @Test
    fun deleteById() = runBlocking {
        val prod = Product(0, "aceite", 6.0f)
        val prod2 = Product(0, "arroz", 7.0f)
        val prod3 = Product(0, "lejia", 4.0f)
        val prod4 = Product(0, "leche", 5.0f)
        val prods = listOf(prod, prod2, prod3, prod4)

        repository.addList(prods)

        val savedProds = repository.getAll()

        val id = savedProds[2].id
        repository.deleteById(id)

        assertTrue(repository.getSize() == 3)
        assertTrue(repository.getById(id) == null)

    }

    @Test
    fun getException() = runBlocking {

        val failProductEntityDao = FakeProductEntityDao(true)
        repository = RepositoryImpl(failProductEntityDao)

        val prod = Product(0, "aceite", 6.0f)

        try {
            repository.add(prod)
        } catch(e: RuntimeException) {
            val ex = e.cause?.message
        }
    }

}