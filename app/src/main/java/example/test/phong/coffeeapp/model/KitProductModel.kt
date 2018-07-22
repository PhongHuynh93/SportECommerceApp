package example.test.phong.coffeeapp.model

import example.test.phong.coffeeapp.BaseTypeModel

data class KitProductModel(val arrayListOf: ArrayList<String>): BaseTypeModel {
    override fun getType() = BaseTypeModel.KIT_PRODUCT
}
