package example.test.phong.coffeeapp


import android.content.Context
import android.graphics.Paint
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import example.test.phong.coffeeapp.model.QuantityProductModel
import example.test.phong.coffeeapp.utils.load
import example.test.phong.coffeeapp.utils.setUpToolbar
import kotlinx.android.synthetic.main.fragment_cart.*
import kotlinx.android.synthetic.main.item_cart.view.*


class CartFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity!!.setUpToolbar(toolbar)
        NavigationUI.setupWithNavController(toolbar, Navigation.findNavController(view))
        rcv?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = CartAdapter()
            addItemDecoration(CartSpacingDecoration(context = context, adapter = adapter as CartAdapter))
        }
    }
}

class CartSpacingDecoration(val adapter: CartAdapter, val context: Context) : RecyclerView.ItemDecoration() {
    private var mSpace = context.resources.getDimension(R.dimen.space_medium)
    private val mDividerPaint = Paint(Paint.ANTI_ALIAS_FLAG)
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

        outRect.top = context.resources.getDimension(R.dimen.space_large).toInt()

        //        adapter.getItemViewType(adapterPosition).apply {
        //            when (this) {
        //                BaseTypeModel.SIMPLE_TEXT -> {
        //                    outRect.top = context.resources.getDimension(R.dimen.space_large).toInt()
        //                    outRect.bottom = context.resources.getDimension(R.dimen.space_small).toInt()
        //                }
        //                BaseTypeModel.CART_PRODUCT -> {
        //                    outRect.top = context.resources.getDimension(R.dimen.space_medium).toInt()
        //                }
        //                BaseTypeModel.SIMPLE_QUANTITY -> {
        //                    outRect.top = context.resources.getDimension(R.dimen.space_medium).toInt()
        //                    outRect.bottom = context.resources.getDimension(R.dimen.space_medium).toInt()
        //                }
        //                else -> {
        //
        //                }
        //            }
        //        }
    }
}

class CartAdapter : RecyclerView.Adapter<BaseProductVH>() {
    private val mDataList: MutableList<BaseTypeModel> = ArrayList()

    init {
        analyzeData()
    }

    private fun analyzeData() {
        //        mDataList.add(TextModel("Your Cart"))
        mDataList.add(CartModel())
        mDataList.add(QuantityProductModel(1))
        mDataList.add(DividerModel())
        mDataList.add(CartModel())
        mDataList.add(QuantityProductModel(10))
    }

    override fun onBindViewHolder(holder: BaseProductVH, position: Int) {
        holder.bindModel(mDataList[position], position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseProductVH {
        when (viewType) {
            BaseTypeModel.CART_PRODUCT -> {
                return CartVH(LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false))
            }
            BaseTypeModel.SIMPLE_QUANTITY -> {
                return SimpleQuanlityProductVH(LayoutInflater.from(parent.context).inflate(R.layout.item_quantity, parent, false))
            }
            BaseTypeModel.SIMPLE_TEXT -> {
                return SimpleTextVH(LayoutInflater.from(parent.context).inflate(R.layout.item_text, parent, false))
            }
            BaseTypeModel.DIVIDER -> {
                return DividerVH(LayoutInflater.from(parent.context).inflate(R.layout.item_divider, parent, false))
            }
        }
        throw IllegalStateException()
    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    override fun getItemViewType(position: Int): Int {
        return mDataList[position].getType()
    }
}

class DividerModel : BaseTypeModel {
    override fun getType(): Int {
        return BaseTypeModel.DIVIDER
    }

}

class DividerVH(view: View) : BaseProductVH(view) {
    override fun bindModel(model: BaseTypeModel, position: Int) {
    }

}

class CartVH(itemView: View) : BaseProductVH(itemView) {
    override fun bindModel(model: BaseTypeModel, position: Int) {
        if (model is CartModel) {
            itemView.imgvThumb.load(model.thumb)
        }
    }

}
