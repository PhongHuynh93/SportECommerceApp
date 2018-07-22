package example.test.phong.coffeeapp


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import example.test.phong.coffeeapp.model.ProductModel
import example.test.phong.coffeeapp.utils.load
import example.test.phong.coffeeapp.utils.setUpToolbar
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.item_product.view.*

class SearchFragment : Fragment() {
    private val TAG = "SearchFragment"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity!!.setUpToolbar(toolbar)

        appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, offset ->
            // hide the text when scroll
            Log.e(TAG, "offset $offset")
            val alpha = 1 + offset / rootSearchKit.height.toFloat()
            Log.e(TAG, "alpha $alpha")
            rootSearchKit.alpha = alpha
        })

        rcv?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = SearchAdapter {
                val testUrl = "https://media.gucci.com/style/DarkGray_Center_0_0_650x650/1519963209/457095_X5L89_9234_001_100_0000_Light-Oversize-T-shirt-with-Gucci-logo.jpg"
                val bundle = bundleOf(DetailFragment.ARGUMENT_KEY to ProductModel(testUrl, "Brasil Home 2018", "$165"))
                Navigation.findNavController(it).navigate(R.id.action_searchFragment_to_detailFragment, bundle)
            }
        }
    }
}

class SearchAdapter(val listener:(view: View) -> Unit) : RecyclerView.Adapter<SearchVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchVH {
        val searchVH = SearchVH(LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false))
        searchVH.itemView.setOnClickListener {
            listener(it)
        }
        return searchVH
    }

    override fun getItemCount(): Int {
        return 100
    }

    override fun onBindViewHolder(holder: SearchVH, position: Int) {
        holder.itemView.imgvThumb.load("https://media.gucci.com/style/DarkGray_Center_0_0_650x650/1519963209/457095_X5L89_9234_001_100_0000_Light-Oversize-T-shirt-with-Gucci-logo.jpg")
    }

}

class SearchVH(view: View) : RecyclerView.ViewHolder(view) {

}
