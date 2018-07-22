package example.test.phong.coffeeapp.model

import example.test.phong.coffeeapp.BaseTypeModel

class ButtonTextModel(buttonText: String): BaseTypeModel {
    override fun getType() = BaseTypeModel.SIMPLE_BUTTON
}
