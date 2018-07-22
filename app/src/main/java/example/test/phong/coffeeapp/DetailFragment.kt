package example.test.phong.coffeeapp


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import example.test.phong.coffeeapp.model.*
import example.test.phong.coffeeapp.utils.load
import example.test.phong.coffeeapp.utils.setUpToolbar
import example.test.phong.coffeeapp.view.SquareImageView
import kotlinx.android.synthetic.main.fragment_detail.*


class DetailFragment : Fragment() {
    companion object {
        val ARGUMENT_KEY = "argument_key"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity!!.setUpToolbar(toolbar)
        val firstThumb = arguments!!.getParcelable<ProductModel>(ARGUMENT_KEY).thumb

        vg?.apply {
            adapter = DetailPagerAdapter(firstThumb)
        }

        rcv?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = DetailAdapter()
        }
    }
}

class DetailAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val mDataList: MutableList<BaseTypeModel> = ArrayList()

    init {
        analyzeData()
    }

    private fun analyzeData() {
        mDataList.add(SizeProductModel(arrayListOf(
                StateSizeProduct(false, "s"),
                StateSizeProduct(true, "m"),
                StateSizeProduct(true, "l"),
                StateSizeProduct(false, "xl"),
                StateSizeProduct(false, "xxl"))))

        mDataList.add(KitProductModel(arrayListOf("home", "away", "third")))
        mDataList.add(QuantityProductModel())
        mDataList.add(ExpandableTextModel("Customize Your Jersey",
                "Gucci patch in SEGA font, used with permission of Sega Holdings Co., Ltd.\n" +
                        "Front patch pockets\n" +
                        "100% cotton\n" +
                        "Made in Italy"))
        mDataList.add(ButtonTextModel("Add to cart"))
        mDataList.add(ExpandableTextModel("Product Details",
                "The colors combined with the exaggerated chevron motif on this bowling shirt give a nostalgic retro feel. Displayed in the graphic font of SEGA, the Gucci patch is an added detail that evokes the feeling of worn-in bowling shoes and the distinctive sound of pins dropping against the wood floor."))
        mDataList.add(ExpandableTextModel("Shipping & Returns",
                "Click on STORE LOCATOR to find stores nearest to you\n" +
                        "\n"))
        mDataList.add(HeaderModel("You May Also Like"))
        mDataList.add(RelatedProductModel(arrayListOf(
                ProductModel("https://media.gucci.com/style/DarkGray_South_0_160_316x316/1519961405/469307_X9D35_9230_001_100_0000_Light-Oversize-collared-T-shirt-with-Gucci-logo.jpg", "Brasil Home 2018", "$165"),
                ProductModel("https://zkshirt.com/wp-content/uploads/2018/01/Disney-Mickey-Gucci-Sweatshirt.jpg", "Night Maroon Foamposite Match", "$165"),
                ProductModel("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRLs2WJDg9lCBo19q8DOjYj1QR4z7XrsvjXryI5DgRev4RlQV0h8w", "Black Gucci Foamposite Shirts", "$165"),
                ProductModel("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTLFeHWbEm2C8NjlUZ14JuBugLstmCJe5P9V3Ym8W01Fi-preto", "PENNYS CENTS- Nike Foamposite", "$165"),
                ProductModel("https://res-1.cloudinary.com/italist/image/upload/t_zoom_v2/347764f2fa4d09e4eecc005000839335.jpg", "Vintage Gucci", "$165"),
                ProductModel("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRGJWtB0p_s6ewhWp60zgwJUpt4jYlyho7ND-BqNEJl_BbechYd", "Black Gucci Foamposite Shirts", "$165")
        )))
    }

    override fun getItemViewType(position: Int): Int {
        return mDataList[position].getType()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            BaseTypeModel.SIZE_PRODUCT -> return SizeProductVH(LayoutInflater.from(parent.context).inflate(R.layout.item_size_product, parent, false))
            BaseTypeModel.KIT_PRODUCT -> return KitProductVH(LayoutInflater.from(parent.context).inflate(R.layout.item_kit_product, parent, false))
            BaseTypeModel.SIMPLE_QUANLITY -> return SimpleQuanlityProductVH(LayoutInflater.from(parent.context).inflate(R.layout.item_quantity, parent, false))
            BaseTypeModel.EXPANDABLE_TEXT -> return ExpandableTextVH(LayoutInflater.from(parent.context).inflate(R.layout.item_expandable, parent, false))
            BaseTypeModel.SIMPLE_BUTTON -> return SimpleButtonVH(LayoutInflater.from(parent.context).inflate(R.layout.item_button, parent, false))
            BaseTypeModel.SIMPLE_TEXT -> return SimpleTextVH(LayoutInflater.from(parent.context).inflate(R.layout.item_text, parent, false))
            BaseTypeModel.RELATED_PRODUCT -> return RelatedProductVH(LayoutInflater.from(parent.context).inflate(R.layout.item_related_product, parent, false))
            else -> {
                throw IllegalStateException()
            }
        }
    }

    override fun getItemCount() = mDataList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

    }

}

class RelatedProductVH(view: View) : RecyclerView.ViewHolder(view) {

}

class SimpleTextVH(view: View) : RecyclerView.ViewHolder(view) {

}

class SimpleButtonVH(view: View) : RecyclerView.ViewHolder(view) {

}

class ExpandableTextVH(view: View) : RecyclerView.ViewHolder(view) {

}

class SimpleQuanlityProductVH(view: View) : RecyclerView.ViewHolder(view) {

}

class KitProductVH(view: View) : RecyclerView.ViewHolder(view) {

}

class SizeProductVH(view: View) : RecyclerView.ViewHolder(view) {

}

class DetailPagerAdapter(private val firstThumb: String) : PagerAdapter() {
    override fun isViewFromObject(arg0: View, arg1: Any): Boolean {
        return arg0 == arg1
    }

    override fun getCount(): Int {
        return 3
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        return SquareImageView(container.context).apply {
            layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            load(firstThumb, true)
            container.addView(this)
        }
    }

    override fun destroyItem(container: ViewGroup, position: Int, child: Any) {
        container.removeView(child as ImageView)
    }
}
