package example.test.phong.coffeeapp.model

import example.test.phong.coffeeapp.BaseTypeModel

class RelatedProductModel(val listRelatedProduct: ArrayList<ProductModel>): BaseTypeModel {
    override fun getType() = BaseTypeModel.RELATED_PRODUCT
}
