package example.test.phong.coffeeapp.model

import example.test.phong.coffeeapp.BaseTypeModel

data class TextModel(val text: String): BaseTypeModel {
    override fun getType() = BaseTypeModel.SIMPLE_TEXT
}
