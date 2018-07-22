package example.test.phong.coffeeapp.model

import example.test.phong.coffeeapp.BaseTypeModel

class HeaderModel(text: String): BaseTypeModel {
    override fun getType() = BaseTypeModel.SIMPLE_TEXT
}
