package example.test.phong.coffeeapp


import android.content.Context
import android.graphics.*
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import example.test.phong.coffeeapp.BaseTypeModel.Companion.EXPANDABLE_TEXT_CHILD
import example.test.phong.coffeeapp.BaseTypeModel.Companion.EXPANDABLE_TEXT_PARENT
import example.test.phong.coffeeapp.BaseTypeModel.Companion.EXPANDABLE_TEXT_PARENT_DIVIDER
import example.test.phong.coffeeapp.BaseTypeModel.Companion.NAME_PRODUCT
import example.test.phong.coffeeapp.BaseTypeModel.Companion.RELATED_PRODUCT
import example.test.phong.coffeeapp.BaseTypeModel.Companion.SIMPLE_BUTTON
import example.test.phong.coffeeapp.BaseTypeModel.Companion.SIMPLE_QUANTITY
import example.test.phong.coffeeapp.DetailFragment.Companion.CLOSE
import example.test.phong.coffeeapp.DetailFragment.Companion.OPEN
import example.test.phong.coffeeapp.model.*
import example.test.phong.coffeeapp.utils.*
import example.test.phong.coffeeapp.view.SquareImageView
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.android.synthetic.main.item_button.view.*
import kotlinx.android.synthetic.main.item_expandable_child.view.*
import kotlinx.android.synthetic.main.item_expandable_parent.view.*
import kotlinx.android.synthetic.main.item_name_product.view.*
import kotlinx.android.synthetic.main.item_quantity.view.*
import kotlinx.android.synthetic.main.item_related_product_overview.view.*
import kotlinx.android.synthetic.main.item_related_product_rcv.view.*
import kotlinx.android.synthetic.main.item_size_product.view.*


class DetailFragment : Fragment() {
    private var currentQuantity: Int = 0
    private lateinit var mAdapter: DetailAdapter
    private lateinit var mLayoutManager: LinearLayoutManager
    private lateinit var mChosenProduct: ProductModel
    private lateinit var mCartIcon: View
    private lateinit var mQuantityIcon: TextView

    companion object {
        val ARGUMENT_KEY = "argument_key"
        const val OPEN = 180.0f
        const val CLOSE = 0.0f
    }

    private val mOnClick = View.OnClickListener {
        when (it.id) {
            R.id.tvIncrease -> {
                val pos = it.getParentTagInt()
                val itemAtPos = mAdapter.getItemAtPos(pos)
                if (itemAtPos is QuantityProductModel) {
                    val vh = rcv.findViewHolderForAdapterPosition(pos)
                    vh?.let {
                        vh.itemView.tvQuantity.setCurrentText(currentQuantity.toString())
                        currentQuantity = itemAtPos.quantity + 1
                        itemAtPos.quantity = currentQuantity
                        vh.itemView.tvQuantity.setText(currentQuantity.toString())
                    }
                }
            }
            R.id.tvDecrease -> {
                val pos = it.getParentTagInt()
                val itemAtPos = mAdapter.getItemAtPos(pos)
                if (itemAtPos is QuantityProductModel) {
                    val vh = rcv.findViewHolderForAdapterPosition(pos)
                    vh?.let {
                        vh.itemView.tvQuantity.setCurrentText(currentQuantity.toString())
                        currentQuantity = Math.max(0, itemAtPos.quantity - 1)
                        itemAtPos.quantity = currentQuantity
                        vh.itemView.tvQuantity.setText(currentQuantity.toString())
                    }
                }
            }
            R.id.bnAddToCart -> {
                if (currentQuantity <= 0) {
                    Toast.makeText(context, "Please add some", Toast.LENGTH_SHORT).show()
                    return@OnClickListener
                }
                if (imgvFakeProduct.visibility == View.VISIBLE) return@OnClickListener
                if (imgvFakeProduct.drawable != null) {
                    animateFakeProductViewToCart(it)
                    return@OnClickListener
                }
                val requestOptions = RequestOptions
                        .overrideOf(resources.getDimension(R.dimen.size_fake_product).toInt())
                        .onlyRetrieveFromCache(true)
                Glide.with(this)
                        .asBitmap()
                        .load(mChosenProduct.thumb)
                        .apply(requestOptions)
                        .into(object : SimpleTarget<Bitmap>() {
                            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                                val drawable = RoundedBitmapDrawableFactory.create(resources, resource)
                                drawable.isCircular = true
                                imgvFakeProduct.setImageDrawable(drawable)
                                animateFakeProductViewToCart(it)
                            }
                        })
            }
        }
    }

    // TODO: 7/29/2018 understand how to calculate the bezier curve here
    private fun animateFakeProductViewToCart(startView: View) {
        val toLocation = IntArray(2)
        val fromLocation = IntArray(2)
        mCartIcon.getLocationInWindow(toLocation)
        startView.getLocationInWindow(fromLocation)

        imgvFakeProduct.x = startView.width / 2f + startView.x
        imgvFakeProduct.y = fromLocation[1].toFloat() - startView.height / 2f
        imgvFakeProduct.visibility = View.VISIBLE

        imgvFakeProduct
                .animate()
                .x(toLocation[0].toFloat())
                .y(toLocation[1].toFloat())
                .setDuration(500)
                .setInterpolator(DecelerateInterpolator())
                .withEndAction {
                    if (currentQuantity <= 0) {
                        mQuantityIcon.visibility = View.GONE
                    } else {
                        mQuantityIcon.text = currentQuantity.toString()
                        mQuantityIcon.visibility = View.VISIBLE
                        // make overshoot anim
                        mCartIcon.scaleX = 0.7f
                        mCartIcon.scaleY = 0.7f
                        mCartIcon
                                .animate()
                                .scaleX(1f)
                                .scaleY(1f)
                                .setDuration(300)
                                .setInterpolator(OvershootInterpolator(5f))
                                .start()
                    }
                    imgvFakeProduct.visibility = View.GONE
                }.start()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity!!.setUpToolbar(toolbar)
        mChosenProduct = arguments!!.getParcelable<ProductModel>(ARGUMENT_KEY)

        vg?.apply {
            adapter = DetailPagerAdapter(mChosenProduct)
        }

        rcv?.apply {
            mLayoutManager = LinearLayoutManager(context)
            layoutManager = mLayoutManager
            mAdapter = DetailAdapter(mChosenProduct, mOnClick)
            adapter = mAdapter
            addItemDecoration(DetailSpacingItemDecoration(context, adapter as DetailAdapter))
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater!!.inflate(R.menu.main, menu)
        Handler().post {
            mCartIcon = toolbar.findViewById<View>(R.id.addToCart)
            val actionView = menu!!.findItem(R.id.addToCart).actionView
            mQuantityIcon = actionView.findViewById<TextView>(R.id.tvCount)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return super.onOptionsItemSelected(item)

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
                } else if (this == EXPANDABLE_TEXT_PARENT_DIVIDER ||
                        (this == EXPANDABLE_TEXT_PARENT)) {
                } else if (this == SIMPLE_QUANTITY) {
                    outRect.top = mSpaceMedium.toInt()
                    outRect.bottom = mSpaceMedium.toInt()
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
                    private val mOnClick: View.OnClickListener) : RecyclerView.Adapter<BaseProductVH>() {
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
                vh.itemView.tvIncrease.setOnClickListener(mOnClick)
                vh.itemView.tvDecrease.setOnClickListener(mOnClick)
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
            BaseTypeModel.SIMPLE_BUTTON -> {
                val vh = SimpleButtonVH(LayoutInflater.from(parent.context).inflate(R.layout.item_button, parent, false))
                vh.itemView.bnAddToCart.setOnClickListener(mOnClick)
                return vh
            }
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
        holder.bindModel(mDataList[position], position)
    }

    fun getItemAtPos(pos: Int): BaseTypeModel {
        return mDataList[pos]
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

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)
}


abstract class BaseProductVH(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun bindModel(model: BaseTypeModel, position: Int)
}

class NameProductVH(view: View) : BaseProductVH(view), Divider {
    override fun bindModel(model: BaseTypeModel, position: Int) {
        if (model is ProductModel) {
            itemView.tvName.text = model.name
            itemView.tvBrand.text = model.brand
            itemView.tvPrice.text = model.price
        }
    }
}

class SizeProductVH(view: View) : BaseProductVH(view), Divider {
    override fun bindModel(model: BaseTypeModel, position: Int) {
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
    override fun bindModel(model: BaseTypeModel, position: Int) {
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
    override fun bindModel(model: BaseTypeModel, position: Int) {
    }
}

class SimpleButtonVH(view: View) : BaseProductVH(view), Divider {
    override fun bindModel(model: BaseTypeModel, position: Int) {
    }
}

open class ExpandableTextVH(view: View) : BaseProductVH(view) {

    override fun bindModel(model: BaseTypeModel, position: Int) {
        if (model is ExpandableTextModel) {
            itemView.tvExpand.setTextFuture(model.showedText)
        }
    }
}

class ExpandableTextDividerVH(view: View) : ExpandableTextVH(view), Divider

class ExpandableChildTextVH(view: View) : BaseProductVH(view) {
    override fun bindModel(model: BaseTypeModel, position: Int) {
        if (model is ExpandableTextModelChild) {
            itemView.tvHiddenText.setTextFuture(model.hiddenText)
        }
    }
}

class SimpleQuanlityProductVH(view: View) : BaseProductVH(view) {
    override fun bindModel(model: BaseTypeModel, position: Int) {
        if (model is QuantityProductModel) {
            itemView.tag = position
            itemView.tvQuantity.setCurrentText(model.quantity.toString())
        }
    }
}

class KitProductVH(view: View) : BaseProductVH(view) {
    override fun bindModel(model: BaseTypeModel, position: Int) {
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
