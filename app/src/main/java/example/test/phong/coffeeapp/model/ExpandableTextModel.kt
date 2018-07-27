package example.test.phong.coffeeapp.model

import example.test.phong.coffeeapp.BaseTypeModel

data class ExpandableTextModel(val showedText: String, val hiddenText: String, var isExpanded: Boolean = false): BaseTypeModel {

    override fun getType() = BaseTypeModel.EXPANDABLE_TEXT_PARENT
}

data class ExpandableTextModelChild(val hiddenText: String): BaseTypeModel {

    override fun getType() = BaseTypeModel.EXPANDABLE_TEXT_CHILD
}
