package example.test.phong.coffeeapp

interface BaseTypeModel {
    fun getType(): Int

    companion object {
        const val NAME_PRODUCT: Int = 7
        const val SIZE_PRODUCT: Int = 0
        const val KIT_PRODUCT: Int = 1
        const val SIMPLE_QUANTITY: Int = 2
        const val EXPANDABLE_TEXT_PARENT: Int = 3
        const val SIMPLE_BUTTON: Int = 4
        const val SIMPLE_TEXT: Int = 5
        const val RELATED_PRODUCT: Int = 6
        const val EXPANDABLE_TEXT_CHILD: Int = 8
        const val EXPANDABLE_TEXT_PARENT_DIVIDER: Int = 9
        const val CART_PRODUCT: Int = 10
        const val DIVIDER: Int = 11

    }
}
