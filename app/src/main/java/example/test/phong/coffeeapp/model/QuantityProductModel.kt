package example.test.phong.coffeeapp.model

import example.test.phong.coffeeapp.BaseTypeModel

data class QuantityProductModel(var quantity: Int): BaseTypeModel {
    override fun getType() = BaseTypeModel.SIMPLE_QUANTITY
}

