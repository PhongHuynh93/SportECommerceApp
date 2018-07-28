package example.test.phong.coffeeapp.utils

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.target.Target
import example.test.phong.coffeeapp.R



fun ImageView.load(url: String, loadOnlyFromCache: Boolean = false, onLoadingFinished: () -> Unit = {}) {
    val listener = object : RequestListener<Drawable> {
        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
            onLoadingFinished()
            return false
        }

        override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
            onLoadingFinished()
            return false
        }
    }
    val requestOptions = RequestOptions.placeholderOf(R.drawable.image_placeholder)
            .dontTransform()
            .onlyRetrieveFromCache(loadOnlyFromCache)
    Glide.with(this)
            .load(url)
            .apply(requestOptions)
            .listener(listener)
            .into(this)
}

fun ImageView.loadRounded(url: String, radius: Float) {
    Glide.with(context)
            .asBitmap()
            .load(url)
            .into(object : BitmapImageViewTarget(this) {
                override fun setResource(resource: Bitmap?) {
                    val drawable = RoundedBitmapDrawableFactory.create(resources, resource)
                    drawable.cornerRadius = radius
//                    drawable.isCircular = true
                    setImageDrawable(drawable)
                }
            })
}