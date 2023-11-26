package com.manuelsoft.data

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.manuelsoft.data.entity.CategoryEntity
import com.manuelsoft.data.entity.ProductEntity
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

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var db: RoomDb
    private lateinit var productEntityDao: ProductEntityDao
    private lateinit var categoryEntityDao: CategoryEntityDao
    private lateinit var productWithCategoryDao: ProductWithCategoryDao

    @Before
    fun createDB() {

        //  db = Room.databaseBuilder(appContext, RoomDb::class.java, "ProductsDb").build()
        hiltRule.inject()
        productEntityDao = db.productDao()
        categoryEntityDao = db.categoryDao()
        productWithCategoryDao = db.productWithCategoryDao()

    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.clearAllTables()
        db.close()
    }

    @Test
    fun insert_listOfProducts_returnsPositive() = runBlocking {

        val category = CategoryEntity(2, "gato")
        val product = ProductEntity(2, "arroz", 2, 1.50f)

        categoryEntityDao.addCategory(category)
        productEntityDao.addProduct(product)
        productWithCategoryDao.insert()

        val c = categoryEntityDao.getAll()
        val p = productEntityDao.getAll()
        val n = productWithCategoryDao.getAllProductsWithCategories()

        assertTrue(c.isEmpty())
        assertTrue(p.isEmpty())
        assertTrue(n.isEmpty())
    }


}