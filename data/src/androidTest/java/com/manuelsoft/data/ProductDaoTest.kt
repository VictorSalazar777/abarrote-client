package com.manuelsoft.data

import android.content.Context
import android.database.sqlite.SQLiteException
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException


@MediumTest
@RunWith(AndroidJUnit4::class)

class ProductDaoTest {

    private lateinit var db: RoomDb
    private lateinit var productDao: ProductDao

    @Before
    fun createDB() {
        val appContext = ApplicationProvider.getApplicationContext<Context>()
      //  db = Room.inMemoryDatabaseBuilder(appContext, RoomDb::class.java).build()
        db = Room.databaseBuilder(appContext, RoomDb::class.java, "ProductsDb").build()
        productDao = db.productDao()

    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test(expected = SQLiteException::class)
    fun insert_listOfProductsWithSameId_throwsException() = runBlocking {
        val prod = Product(0, "clorox", 6.0f)
        val prod2 = Product(0, "clorox", 6.0f)
        val prod3 = Product(0, "clorox", 6.0f)
        val prod4 = Product(0, "leche", 6.0f)

        val prods = listOf(prod, prod2, prod3, prod4)

        productDao.insert(prods)

    }


    @Test
    fun insert_listOfProducts_returnsPositive() = runBlocking {
        val prod = Product(1, "clorox", 6.0f)
        val prod2 = Product(2, "clorox", 6.0f)
        val prod3 = Product(3, "clorox", 6.0f)
        val prod4 = Product(4, "leche", 6.0f)

        val prods = listOf(prod, prod2, prod3, prod4)

        productDao.insert(prods)

        val n = productDao.getSize()

        assertTrue(n > 0 && n == 4)
    }

    @Test
    fun getAll_insertingProducts_gettingSameProducts() = runBlocking {
        val prod = Product(1, "clorox", 6.0f)
        val prod2 = Product(2, "aceite", 6.0f)
        val prod3 = Product(3, "leche", 6.0f)
        productDao.insert(prod)
        productDao.insert(prod2)
        productDao.insert(prod3)

        val list = productDao.getAll().first()

        assertTrue(list == listOf(prod, prod2, prod3))
    }

    @Test
    fun deleteAll_deletingAllProducts_positiveReturned() = runBlocking {
        val prod = Product(1, "clorox", 6.0f)
        val prod2 = Product(2, "clorox", 6.0f)
        val prod3 = Product(3, "clorox", 6.0f)
        val prod4 = Product(4, "leche", 6.0f)

        val prods = listOf(prod, prod2, prod3, prod4)

        productDao.insert(prods)

        val n = productDao.deleteAll()

        assertTrue(n == 4)
    }


}