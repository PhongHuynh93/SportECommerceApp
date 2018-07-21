package example.test.phong.coffeeapp


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.google.android.material.appbar.AppBarLayout
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

        if (activity is AppCompatActivity) {
            (activity as AppCompatActivity).setSupportActionBar(toolbar)

            val actionbar: ActionBar? = (activity as AppCompatActivity).supportActionBar
            actionbar?.apply {
                setDisplayHomeAsUpEnabled(true)
                setHomeAsUpIndicator(R.drawable.ic_hamguber)
                setTitle(R.string.title_product_page)
            }
        }

        appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, offset ->
            // hide the text when scroll
            Log.e(TAG, "offset $offset")
            val alpha = 1 + offset / rootSearchKit.height.toFloat()
            Log.e(TAG, "alpha $alpha")
            rootSearchKit.alpha = alpha
        })

        rcv?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = SearchAdapter(Glide.with(this))
        }
    }
}

class SearchAdapter(private val requestManager: RequestManager) : RecyclerView.Adapter<SearchVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchVH {
        return SearchVH(LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false))
    }

    override fun getItemCount(): Int {
        return 100
    }

    override fun onBindViewHolder(holder: SearchVH, position: Int) {
        requestManager.load("https://vn-live.slatic.net/original/c4cc287e5e7d3cbb4f9babdb1c16d1b1.jpg").into(holder.itemView.imgvThumb)
    }

}

class SearchVH(view: View) : RecyclerView.ViewHolder(view) {

}
