package com.manuelsoft.data

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
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
class ProductEntityDaoTest {

    @get:Rule var hiltRule = HiltAndroidRule(this)
    @Inject lateinit var db: RoomDb
    private lateinit var productEntityDao: ProductEntityDao

    @Before
    fun createDB() {

      //  db = Room.databaseBuilder(appContext, RoomDb::class.java, "ProductsDb").build()
        hiltRule.inject()
        productEntityDao = db.productDao()

    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun insert_listOfProducts_returnsPositive() = runBlocking {
        val prod = ProductEntity(1, "clorox", 6.0f)
        val prod2 = ProductEntity(2, "clorox", 6.0f)
        val prod3 = ProductEntity(3, "clorox", 6.0f)
        val prod4 = ProductEntity(4, "leche", 6.0f)

        val prods = listOf(prod, prod2, prod3, prod4)

        productEntityDao.insert(prods)

        val n = productEntityDao.getSize()

        assertTrue(n > 0 && n == 4)
    }

    @Test
    fun getAll_insertingProducts_gettingSameProducts() = runBlocking {
        val prod = ProductEntity(1, "clorox", 6.0f)
        val prod2 = ProductEntity(2, "aceite", 6.0f)
        val prod3 = ProductEntity(3, "leche", 6.0f)
        productEntityDao.insert(prod)
        productEntityDao.insert(prod2)
        productEntityDao.insert(prod3)

        val list = productEntityDao.getAll()

        assertTrue(list == listOf(prod, prod2, prod3))
    }

    @Test
    fun deleteAll_deletingAllProducts_positiveReturned() = runBlocking {
        val prod = ProductEntity(1, "clorox", 6.0f)
        val prod2 = ProductEntity(2, "clorox", 6.0f)
        val prod3 = ProductEntity(3, "clorox", 6.0f)
        val prod4 = ProductEntity(4, "leche", 6.0f)

        val prods = listOf(prod, prod2, prod3, prod4)

        productEntityDao.insert(prods)

        productEntityDao.deleteAll()

        assertTrue(productEntityDao.getSize() == 0)
    }



}