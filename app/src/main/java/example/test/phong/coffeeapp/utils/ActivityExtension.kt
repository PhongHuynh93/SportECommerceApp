package example.test.phong.coffeeapp.utils

import android.app.Activity
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import example.test.phong.coffeeapp.R

fun Activity.setUpToolbar(toolbar: Toolbar) {
    if (this is AppCompatActivity) {
        setSupportActionBar(toolbar)

        val actionbar: ActionBar? = supportActionBar
        actionbar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_hamguber)
            setTitle(R.string.title_product_page)
        }
    }
}