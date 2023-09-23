package com.manuelsoft.repository

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.manuelsoft.data.RoomDb
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject


@MediumTest
@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class IntegrationTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)
    @Inject
    lateinit var db: RoomDb
    @Inject
    lateinit var repository: Repository

    @Before
    fun setup() {
        hiltRule.inject()
       // db.clearAllTables()
    }

    @Test
    fun getAll() = runBlocking {
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


}