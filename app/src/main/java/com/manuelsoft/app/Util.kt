package com.manuelsoft.app

import android.content.Context
import android.os.Parcelable
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.manuelsoft.domain_model.model.Category
import com.manuelsoft.domain_model.model.ProductWithCategory
import com.manuelsoft.domain_usecases.CrudResult
import com.manuelsoft.domain_usecases.MessageType
import kotlinx.parcelize.Parcelize


fun showMessageDialog(context: Context, msg: String, detail: String = "") {

    var message = ""

    if (detail.isNotBlank()) {
        message = "$msg: $detail"
    }

    MaterialAlertDialogBuilder(context)
        .setMessage(message)
        .setPositiveButton("Ok") { dialog, _ ->
            dialog.dismiss()
        }
        .show()
}


fun mapCategoryToCategoryP(category: Category): CategoryP {
    return CategoryP(category.id, category.name)
}

fun mapCategoryPToCategory(categoryP: CategoryP): Category {
    return Category(categoryP.id, categoryP.name)
}

fun mapProductWithCategoryToProductWithCategoryP(productWithCategory: ProductWithCategory): ProductWithCategoryP {
    return ProductWithCategoryP(
        productWithCategory.id,
        productWithCategory.name,
        productWithCategory.price,
        productWithCategory.categoryId,
        productWithCategory.categoryName
    )
}

fun mapProductWithCategoryPToProductWithCategory(product: ProductWithCategoryP): ProductWithCategory {
    return ProductWithCategory(
        product.id,
        product.name,
        product.price,
        product.categoryId,
        product.categoryName
    )
}

@Parcelize
data class CategoryP(
    val id: Int,
    val name: String
) : Parcelable

@Parcelize
data class ProductWithCategoryP(
    val id: Int,
    val name: String,
    val price: Float,
    val categoryId: Int,
    val categoryName: String
) : Parcelable

@Parcelize
data class CategoriesAndProduct(
    val categories: List<CategoryP>,
    val product: ProductWithCategoryP
) : Parcelable

@Parcelize
data class Categories(
    val categories: List<CategoryP>
) : Parcelable

fun <T> handleErrorCrudResult(context: Context, crudResult: CrudResult<T>) {
    val type = (crudResult as CrudResult.Failure).messageType
    val msg = (crudResult as CrudResult.Failure).message

    when (type) {

        MessageType.PRODUCT_ADD_ERROR -> {
            showMessageDialog(
                context,
                "Ocurrió un error al agregar producto",
                msg
            )
        }

        MessageType.PRODUCT_UPDATE_ERROR -> {
            showMessageDialog(
                context,
                "Ocurrió un error al modificar producto",
                msg
            )
        }

        MessageType.PRODUCT_REMOVE_ERROR -> {
            showMessageDialog(
                context,
                "Ocurrió un error al remover producto",
                msg
            )
        }

        MessageType.CATEGORY_QUERY_ERROR -> {
            showMessageDialog(
                context,
                "Ocurrió un error al consultar categoría",
                msg
            )
        }

        MessageType.CATEGORY_UPDATE_ERROR -> {
            showMessageDialog(
                context,
                "Ocurrió un error al modificar categoría",
                msg
            )
        }

        MessageType.CATEGORY_REMOVE_ERROR -> {
            showMessageDialog(
                context,
                "Ocurrió un error al remover categoría",
                msg
            )
        }

        else -> {
            showMessageDialog(context, "Ocurrió un error", msg)
        }
    }
}

fun <T> handleSuccessCrudResult(context: Context, crudResult: CrudResult<T>) {
    val type = (crudResult as CrudResult.Success).messageType

    when (type) {
        MessageType.PRODUCT_ADDED_SUCCESSFULLY -> {
            showMessageDialog(
                context,
                "Producto agregado satisfactoriamente"
            )
        }

        MessageType.PRODUCT_UPDATED_SUCCESSFULLY -> {
            showMessageDialog(
                context,
                "Producto modificado satisfactoriamente"
            )
        }

        MessageType.PRODUCT_REMOVED_SUCCESSFULLY -> {
            showMessageDialog(
                context,
                "Producto removido satisfactoriamente"
            )
        }

        MessageType.CATEGORY_ADDED_SUCCESSFULLY -> {
            showMessageDialog(
                context,
                "Categoría agregada satisfactoriamente"
            )
        }

        MessageType.CATEGORY_UPDATED_SUCCESSFULLY -> {
            showMessageDialog(
                context,
                "Categoría modificada satisfactoriamente"
            )
        }

        MessageType.CATEGORY_REMOVED_SUCCESSFULLY -> {
            showMessageDialog(
                context,
                "Categoría removida satisfactoriamente"
            )
        }

        else -> {
            showMessageDialog(
                context,
                "Operación realizada satisfactoriamente"
            )
        }
    }
}