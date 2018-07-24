package example.test.phong.coffeeapp.model

import android.os.Parcelable
import example.test.phong.coffeeapp.BaseTypeModel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductModel(val thumb: String, val name: String, val brand: String, val price: String): Parcelable, BaseTypeModel {
    override fun getType(): Int {
        return BaseTypeModel.NAME_PRODUCT
    }
}