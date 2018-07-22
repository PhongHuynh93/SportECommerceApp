package example.test.phong.coffeeapp.model

import example.test.phong.coffeeapp.BaseTypeModel

data class SizeProductModel(val listSize: ArrayList<StateSizeProduct>): BaseTypeModel {
    override fun getType() = BaseTypeModel.SIZE_PRODUCT
}

data class StateSizeProduct(val available: Boolean, val sizeName: String)
