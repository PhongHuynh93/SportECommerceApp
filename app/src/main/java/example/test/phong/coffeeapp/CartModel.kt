package example.test.phong.coffeeapp

data class CartModel(val thumb: String = "https://i.pinimg.com/originals/5d/56/a8/5d56a80ab39b1f760e0b5d42a3aa9554.jpg") : BaseTypeModel {
    override fun getType(): Int {
        return BaseTypeModel.CART_PRODUCT
    }

}
