package example.test.phong.coffeeapp


import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.view.*
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import example.test.phong.coffeeapp.model.ProductModel
import example.test.phong.coffeeapp.utils.loadRounded
import example.test.phong.coffeeapp.utils.setTextFuture
import example.test.phong.coffeeapp.utils.setUpToolbar
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.item_product.view.*

class SearchFragment : Fragment() {
    private val TAG = "SearchFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater!!.inflate(R.menu.main, menu)

        Handler().post {
            val cartIcon = toolbar.findViewById<View>(R.id.cartFragment)
            cartIcon.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.cartFragment))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item, Navigation.findNavController(activity as Activity, R.id.main_content)) || super.onOptionsItemSelected(item)
    }

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
            rootSearchKit.let {
                val alpha = 1 + offset / rootSearchKit.height.toFloat()
                rootSearchKit.alpha = alpha
            }
        })

        rcv?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = SearchAdapter {
                val testUrl = "https://i.pinimg.com/originals/5d/56/a8/5d56a80ab39b1f760e0b5d42a3aa9554.jpg"
                val bundle = bundleOf(DetailFragment.ARGUMENT_KEY to ProductModel(testUrl, "Brasil Home 2018", "adias", "$165"))
                Navigation.findNavController(it).navigate(R.id.action_searchFragment_to_detailFragment, bundle)
            }
        }
    }
}

class SearchAdapter(val listener: (view: View) -> Unit) : RecyclerView.Adapter<SearchVH>() {
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

    /**
     * <a href="https://medium.com/google-developers/prefetch-text-layout-in-recyclerview-4acf9103f438">
     */
    override fun onBindViewHolder(holder: SearchVH, position: Int) {
        holder.itemView.imgvThumb.loadRounded("https://i.pinimg.com/originals/5d/56/a8/5d56a80ab39b1f760e0b5d42a3aa9554.jpg", holder.itemView.resources.getDimension(R.dimen.radius))
        holder.itemView.tvProductName.setTextFuture("áo thun anroi mẫu 22  áo thun anroi mẫu 22   áo thun anroi mẫu 22")
    }

}

class SearchVH(view: View) : RecyclerView.ViewHolder(view) {

}
