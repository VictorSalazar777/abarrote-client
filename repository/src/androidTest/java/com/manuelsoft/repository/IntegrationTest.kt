package com.manuelsoft.repository

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.manuelsoft.data.RoomDb
import com.manuelsoft.domain_repository.repository.CategoryRepository
import com.manuelsoft.domain_repository.repository.ProductRepository
import com.manuelsoft.domain_repository.repository.ProductWithCategoryRepository
import com.manuelsoft.domain_repository.repository.RepositoryUpdater
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
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
    lateinit var productRepository: ProductRepository

    @Inject
    lateinit var categoryRepository: CategoryRepository

    @Inject
    lateinit var productWithCategoryRepository: ProductWithCategoryRepository

    @Inject
    lateinit var repositoryUpdater: RepositoryUpdater

    @Before
    fun setup() {
        hiltRule.inject()

    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.clearAllTables()
        db.close()
    }

//    @Test
//    fun getAll() = runBlocking {
//        val prod = Product(0, "aceite", 6.0f)
//        val prod2 = Product(0, "arroz", 7.0f)
//        val prod3 = Product(0, "lejia", 4.0f)
//        val prod4 = Product(0, "leche", 5.0f)
//        val tester = listOf(prod, prod2, prod3, prod4)
//
//        productRepository.add(prod)
//        productRepository.add(prod2)
//        productRepository.add(prod3)
//        productRepository.add(prod4)
//
//        val prods = productRepository.getAll()
//
//        assertTrue(prods[0].id == 1)
//        assertTrue(prods[1].id == 2)
//        assertTrue(prods[2].id == 3)
//        assertTrue(prods[3].id == 4)
//
//        for(i in 0 .. 3) {
//            assertTrue(prods[i].name == tester[i].name)
//            assertTrue(prods[i].price == tester[i].price)
//        }
//    }

    @Test
    fun repo() = runBlocking {
//        val cat = Category(1, "aceite")
//        val prod = Product(1, "primor", 1, 6.5f)
//
//        val catres = categoryRepository.add(cat)
//        val prodres = productRepository.add(prod)

        val result = repositoryUpdater.update()

        assertTrue(result.isSuccess)

        productWithCategoryRepository.getAll().collectLatest { it ->
            val v = it.getOrNull()!!

            assertTrue(v.isEmpty())
        }

    }


}