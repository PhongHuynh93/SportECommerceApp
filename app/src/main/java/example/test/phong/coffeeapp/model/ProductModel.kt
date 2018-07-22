package example.test.phong.coffeeapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductModel(val thumb: String, val name: String, val price: String): Parcelable