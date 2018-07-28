package example.test.phong.coffeeapp.model

import example.test.phong.coffeeapp.BaseTypeModel

open class ExpandableTextModel(val showedText: String, val hiddenText: String, var isExpanded: Boolean = false): BaseTypeModel {
    override fun getType() = BaseTypeModel.EXPANDABLE_TEXT_PARENT
}

class ExpandableTextDividerModel(showedText2: String, hiddenText2: String, isExpanded2: Boolean = false): ExpandableTextModel(showedText2, hiddenText2, isExpanded2) {
    override fun getType() = BaseTypeModel.EXPANDABLE_TEXT_PARENT_DIVIDER
}

data class ExpandableTextModelChild(val hiddenText: String): BaseTypeModel {

    override fun getType() = BaseTypeModel.EXPANDABLE_TEXT_CHILD
}
