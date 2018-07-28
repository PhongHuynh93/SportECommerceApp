package example.test.phong.coffeeapp


import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import example.test.phong.coffeeapp.BaseTypeModel.Companion.EXPANDABLE_TEXT_CHILD
import example.test.phong.coffeeapp.BaseTypeModel.Companion.EXPANDABLE_TEXT_PARENT_DIVIDER
import example.test.phong.coffeeapp.BaseTypeModel.Companion.NAME_PRODUCT
import example.test.phong.coffeeapp.BaseTypeModel.Companion.RELATED_PRODUCT
import example.test.phong.coffeeapp.BaseTypeModel.Companion.SIMPLE_BUTTON
import example.test.phong.coffeeapp.DetailFragment.Companion.CLOSE
import example.test.phong.coffeeapp.DetailFragment.Companion.OPEN
import example.test.phong.coffeeapp.model.*
import example.test.phong.coffeeapp.utils.*
import example.test.phong.coffeeapp.view.SquareImageView
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.android.synthetic.main.item_expandable_child.view.*
import kotlinx.android.synthetic.main.item_expandable_parent.view.*
import kotlinx.android.synthetic.main.item_name_product.view.*
import kotlinx.android.synthetic.main.item_quantity.view.*
import kotlinx.android.synthetic.main.item_related_product_overview.view.*
import kotlinx.android.synthetic.main.item_related_product_rcv.view.*
import kotlinx.android.synthetic.main.item_size_product.view.*


class DetailFragment : Fragment() {
    private var currentQuantity: Int = 0

    companion object {
        val ARGUMENT_KEY = "argument_key"
        const val OPEN = 180.0f
        const val CLOSE = 0.0f
    }

    private val mOnClick = View.OnClickListener {

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity!!.setUpToolbar(toolbar)
        val chosenProduct = arguments!!.getParcelable<ProductModel>(ARGUMENT_KEY)

        vg?.apply {
            adapter = DetailPagerAdapter(chosenProduct)
        }

        rcv?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = DetailAdapter(chosenProduct)
            addItemDecoration(DetailSpacingItemDecoration(context, adapter as DetailAdapter))
        }
    }
}

private class DetailSpacingItemDecoration(val context: Context,
                                          val adapter: DetailAdapter) : RecyclerView.ItemDecoration() {
    private var mSpaceMedium: Float = context.resources.getDimension(R.dimen.space_large)
    private var mSpace: Float = context.resources.getDimension(R.dimen.space_medium)
    private val mDividerPaint = Paint(ANTI_ALIAS_FLAG)
    private val mHalfHeight: Float

    init {
        val dividerHeight = context.resources.getDimension(R.dimen.divider_height)
        mDividerPaint.strokeWidth = dividerHeight
        mHalfHeight = dividerHeight / 2
        mDividerPaint.color = ContextCompat.getColor(context, R.color.grey_lightest)
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val adapterPosition = parent.getChildAdapterPosition(view)
        if (adapterPosition == RecyclerView.NO_POSITION) return

        adapter.getItemViewType(adapterPosition).apply {
            if (this != NAME_PRODUCT && this != EXPANDABLE_TEXT_CHILD) {
                if (this == SIMPLE_BUTTON) {
                    outRect.top = mSpaceMedium.toInt()
                    outRect.bottom = mSpaceMedium.toInt()
                } else if (this == EXPANDABLE_TEXT_PARENT_DIVIDER) {
                } else if (this == RELATED_PRODUCT) {
                    outRect.bottom = mSpaceMedium.toInt()
                    outRect.top = mSpaceMedium.toInt()
                } else {
                    outRect.top = mSpaceMedium.toInt()
                }
            }
        }
    }

    override fun onDraw(c: Canvas, rv: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, rv, state)
        val count = rv.childCount
        if (count < 2) return
        val points = FloatArray(count * 4)
        var previousItemNeedsDivider = false

        val layoutManager = rv.layoutManager as LinearLayoutManager
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
        for (i in 0 until count) {
            val holder = rv.findViewHolderForAdapterPosition(firstVisibleItemPosition + i)
            val needsDivider = holder is Divider
            if (previousItemNeedsDivider && needsDivider) {
                points[4 * i] = mSpace
                val top = (layoutManager.getDecoratedTop(holder!!.itemView)
                        + holder.itemView.translationY + mHalfHeight)
                points[4 * i + 1] = top
                points[4 * i + 2] = context.getScreenWidth() - mSpace
                points[4 * i + 3] = top
            }
            previousItemNeedsDivider = needsDivider
        }
        c.drawLines(points, mDividerPaint)
    }
}

class DetailAdapter(private val chosenProduct: ProductModel,
                    private val mOnClick: View.OnClickListener? = null) : RecyclerView.Adapter<BaseProductVH>() {
    private val mDataList: MutableList<BaseTypeModel> = ArrayList()

    init {
        analyzeData()
    }

    private fun analyzeData() {
        mDataList.add(chosenProduct)
        mDataList.add(SizeProductModel(arrayListOf(
                StateSizeProduct(false, SizeType.S),
                StateSizeProduct(true, SizeType.M),
                StateSizeProduct(true, SizeType.L),
                StateSizeProduct(false, SizeType.XL),
                StateSizeProduct(false, SizeType.XXL))))

        mDataList.add(KitProductModel(arrayListOf("home", "away", "third")))
        mDataList.add(QuantityProductModel(0))
        mDataList.add(ExpandableTextModel("Customize Your Jersey",
                                          "Gucci patch in SEGA font, used with permission of Sega Holdings Co., Ltd.\n" +
                                                  "Front patch pockets\n" +
                                                  "100% cotton\n" +
                                                  "Made in Italy"))
        mDataList.add(ButtonTextModel("Add to cart"))
        mDataList.add(ExpandableTextDividerModel("Product Details",
                                                 "The colors combined with the exaggerated chevron motif on this bowling shirt give a nostalgic retro feel. Displayed in the graphic font of SEGA, the Gucci patch is an added detail that evokes the feeling of worn-in bowling shoes and the distinctive sound of pins dropping against the wood floor."))
        mDataList.add(ExpandableTextModel("Shipping & Returns",
                                          "Click on STORE LOCATOR to find stores nearest to you"))
        mDataList.add(HeaderModel("You May Also Like"))
        mDataList.add(RelatedProductModel(arrayListOf(
                ProductModel("https://media.gucci.com/style/DarkGray_South_0_160_316x316/1519961405/469307_X9D35_9230_001_100_0000_Light-Oversize-collared-T-shirt-with-Gucci-logo.jpg",
                             "Brasil Home 2018",
                             "adidas",
                             "$165"),
                ProductModel("https://zkshirt.com/wp-content/uploads/2018/01/Disney-Mickey-Gucci-Sweatshirt.jpg",
                             "Night Maroon Foamposite Match",
                             "adidas",
                             "$165"),
                ProductModel("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRLs2WJDg9lCBo19q8DOjYj1QR4z7XrsvjXryI5DgRev4RlQV0h8w",
                             "Black Gucci Foamposite Shirts",
                             "adidas",
                             "$165"),
                ProductModel("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTLFeHWbEm2C8NjlUZ14JuBugLstmCJe5P9V3Ym8W01Fi-preto",
                             "PENNYS CENTS- Nike Foamposite",
                             "adidas",
                             "$165"),
                ProductModel("https://res-1.cloudinary.com/italist/image/upload/t_zoom_v2/347764f2fa4d09e4eecc005000839335.jpg",
                             "Vintage Gucci",
                             "adidas",
                             "$165"),
                ProductModel("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRGJWtB0p_s6ewhWp60zgwJUpt4jYlyho7ND-BqNEJl_BbechYd",
                             "Black Gucci Foamposite Shirts",
                             "adidas",
                             "$165")
                                                     )))
    }

    override fun getItemViewType(position: Int): Int {
        return mDataList[position].getType()
    }

    private val mOnClickExpand = View.OnClickListener {
        val vh = it.tag as RecyclerView.ViewHolder
        val pos = vh.adapterPosition
        val itemAtPos = mDataList[pos]
        if (itemAtPos is ExpandableTextModel) {
            if (itemAtPos.isExpanded) {
                val startPos = pos + 1
                if (mDataList[startPos].getType() == EXPANDABLE_TEXT_CHILD) {
                    mDataList.removeAt(startPos)
                    vh.itemView.imgvExpand.animate().rotation(CLOSE).start()
                    notifyItemRangeRemoved(startPos, 1)
                    itemAtPos.isExpanded = false
                }
            } else {
                val startPos = pos + 1
                if (mDataList[startPos].getType() != EXPANDABLE_TEXT_CHILD) {
                    mDataList.add(startPos, ExpandableTextModelChild(itemAtPos.hiddenText))
                    vh.itemView.imgvExpand.animate().rotation(OPEN).start()
                    notifyItemRangeInserted(startPos, 1)
                    itemAtPos.isExpanded = true
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseProductVH {
        return when (viewType) {
            BaseTypeModel.NAME_PRODUCT -> NameProductVH(LayoutInflater.from(parent.context).inflate(R.layout.item_name_product, parent, false))
            BaseTypeModel.SIZE_PRODUCT -> SizeProductVH(LayoutInflater.from(parent.context).inflate(R.layout.item_size_product, parent, false))
            BaseTypeModel.KIT_PRODUCT -> KitProductVH(LayoutInflater.from(parent.context).inflate(R.layout.item_kit_product, parent, false))
            BaseTypeModel.SIMPLE_QUANTITY -> {
                val vh = SimpleQuanlityProductVH(LayoutInflater.from(parent.context).inflate(R.layout.item_quantity, parent, false))
                vh.itemView.tvIncrease.setOnClickListener {
                    val itemAtPos = mDataList[vh.adapterPosition]
                    if (itemAtPos is QuantityProductModel) {
                        vh.itemView.tvQuantity.setCurrentText(itemAtPos.quantity.toString())
                        itemAtPos.quantity += 1
                        vh.itemView.tvQuantity.setText(itemAtPos.quantity.toString())
                    }
                }
                vh.itemView.tvDecrease.setOnClickListener {
                    val itemAtPos = mDataList[vh.adapterPosition]
                    if (itemAtPos is QuantityProductModel) {
                        vh.itemView.tvQuantity.setCurrentText(itemAtPos.quantity.toString())
                        itemAtPos.quantity = Math.max(0, itemAtPos.quantity - 1)
                        vh.itemView.tvQuantity.setText(itemAtPos.quantity.toString())
                    }
                }
                return vh
            }
            BaseTypeModel.EXPANDABLE_TEXT_PARENT_DIVIDER -> {
                val vh = ExpandableTextDividerVH(LayoutInflater.from(parent.context).inflate(R.layout.item_expandable_parent, parent, false))
                vh.itemView.tag = vh
                vh.itemView.setOnClickListener(mOnClickExpand)
                return vh
            }
            BaseTypeModel.EXPANDABLE_TEXT_PARENT -> {
                val vh = ExpandableTextVH(LayoutInflater.from(parent.context).inflate(R.layout.item_expandable_parent, parent, false))
                vh.itemView.tag = vh
                vh.itemView.setOnClickListener(mOnClickExpand)
                return vh
            }
            BaseTypeModel.EXPANDABLE_TEXT_CHILD -> return ExpandableChildTextVH(LayoutInflater.from(parent.context).inflate(R.layout.item_expandable_child, parent, false))
            BaseTypeModel.SIMPLE_BUTTON -> SimpleButtonVH(LayoutInflater.from(parent.context).inflate(R.layout.item_button, parent, false))
            BaseTypeModel.SIMPLE_TEXT -> SimpleTextVH(LayoutInflater.from(parent.context).inflate(R.layout.item_text, parent, false))
            BaseTypeModel.RELATED_PRODUCT -> {
                val relatedProductVH = RelatedProductVH(LayoutInflater.from(parent.context).inflate(R.layout.item_related_product_rcv, parent, false))
                relatedProductVH.itemView.rcv.apply {
                    layoutManager = LinearLayoutManager(parent.context, LinearLayoutManager.HORIZONTAL, false).apply {
                        initialPrefetchItemCount = 4 // for child rcv, set 4 items
                    }
                    addItemDecoration(RelatedProductSpacingDecoration())
                    adapter = RelatedProductAdapter()
                }
                return relatedProductVH
            }
            else -> {
                throw IllegalStateException()
            }
        }
    }

    override fun getItemCount() = mDataList.size

    override fun onBindViewHolder(holder: BaseProductVH, position: Int) {
        holder.bindModel(mDataList[position])
    }
}

class RelatedProductSpacingDecoration : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val adapterPosition = parent.getChildAdapterPosition(view)
        if (adapterPosition == RecyclerView.NO_POSITION) return
        outRect.right = parent.context.getDimen(R.dimen.space_small).toInt()
    }
}

class RelatedProductAdapter : RecyclerView.Adapter<RelatedProductAdapter.ViewHolder>() {
    private var mData: MutableList<ProductModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_related_product_overview,
                                                                      parent,
                                                                      false))
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.imgv.load(mData[position].thumb)
        holder.itemView.tvheader.text = mData[position].name
        holder.itemView.tvSubHead.text = mData[position].price
    }


    fun addData(listRelatedProduct: ArrayList<ProductModel>) {
        mData.clear()
        mData.addAll(listRelatedProduct)
        notifyDataSetChanged()
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    }
}


abstract class BaseProductVH(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun bindModel(model: BaseTypeModel)
}

class NameProductVH(view: View) : BaseProductVH(view), Divider {
    override fun bindModel(model: BaseTypeModel) {
        if (model is ProductModel) {
            itemView.tvName.text = model.name
            itemView.tvBrand.text = model.brand
            itemView.tvPrice.text = model.price
        }
    }
}

class SizeProductVH(view: View) : BaseProductVH(view), Divider {
    override fun bindModel(model: BaseTypeModel) {
        if (model is SizeProductModel) {
            itemView.tvSize1.text = model.listSize[0].sizeName.toString()
            itemView.tvSize1.setTextColor(Color.parseColor("#4d455a64"))
            itemView.tvSize1.setBackgroundResource(R.drawable.fill_disable_rect)

            itemView.tvSize2.text = model.listSize[1].sizeName.toString()
            itemView.tvSize2.setTextColor(Color.parseColor("#455a64"))
            itemView.tvSize2.setBackgroundResource(R.drawable.outline_unselect_rect)

            itemView.tvSize3.text = model.listSize[2].sizeName.toString()
            itemView.tvSize3.setTextColor(Color.parseColor("#ffffff"))
            itemView.tvSize3.setBackgroundResource(R.drawable.outline_select_rect)

            itemView.tvSize4.text = model.listSize[3].sizeName.toString()
            itemView.tvSize4.setTextColor(Color.parseColor("#4d455a64"))
            itemView.tvSize4.setBackgroundResource(R.drawable.fill_disable_rect)

            itemView.tvSize5.text = model.listSize[4].sizeName.toString()
            itemView.tvSize5.setTextColor(Color.parseColor("#4d455a64"))
            itemView.tvSize5.setBackgroundResource(R.drawable.fill_disable_rect)
        }
    }
}

class RelatedProductVH(view: View) : BaseProductVH(view) {
    override fun bindModel(model: BaseTypeModel) {
        if (model is RelatedProductModel) {
            itemView.rcv.adapter.apply {
                if (this is RelatedProductAdapter) {
                    this.addData(model.listRelatedProduct)
                }
            }
        }
    }

}

class SimpleTextVH(view: View) : BaseProductVH(view), Divider {
    override fun bindModel(model: BaseTypeModel) {
    }
}

class SimpleButtonVH(view: View) : BaseProductVH(view), Divider {
    override fun bindModel(model: BaseTypeModel) {
    }
}

open class ExpandableTextVH(view: View) : BaseProductVH(view) {

    override fun bindModel(model: BaseTypeModel) {
        if (model is ExpandableTextModel) {
            itemView.tvExpand.setTextFuture(model.showedText)
        }
    }
}

class ExpandableTextDividerVH(view: View) : ExpandableTextVH(view), Divider

class ExpandableChildTextVH(view: View) : BaseProductVH(view) {
    override fun bindModel(model: BaseTypeModel) {
        if (model is ExpandableTextModelChild) {
            itemView.tvHiddenText.setTextFuture(model.hiddenText)
        }
    }
}

class SimpleQuanlityProductVH(view: View) : BaseProductVH(view) {
    override fun bindModel(model: BaseTypeModel) {
        if (model is QuantityProductModel) {
            itemView.tvQuantity.setCurrentText(model.quantity.toString())
        }
    }
}

class KitProductVH(view: View) : BaseProductVH(view) {
    override fun bindModel(model: BaseTypeModel) {
    }
}


class DetailPagerAdapter(private val chosenProduct: ProductModel) : PagerAdapter() {
    override fun isViewFromObject(arg0: View, arg1: Any): Boolean {
        return arg0 == arg1
    }

    override fun getCount(): Int {
        return 3
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        return SquareImageView(container.context).apply {
            layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                                  ViewGroup.LayoutParams.MATCH_PARENT)
            load(chosenProduct.thumb, true)
            container.addView(this)
        }
    }

    override fun destroyItem(container: ViewGroup, position: Int, child: Any) {
        container.removeView(child as ImageView)
    }
}
