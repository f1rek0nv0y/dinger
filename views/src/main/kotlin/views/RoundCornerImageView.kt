package views

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.util.AttributeSet
import android.view.View
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import org.stoyicker.dinger.views.R

class RoundCornerImageView(context: Context, attributeSet: AttributeSet? = null)
  : View(context, attributeSet), Target {
  private val cornerRadiusPx: Float
  private val picasso = Picasso.get()
  private var drawable: Drawable? = null
    set(value) {
      field = value
      postInvalidate()
    }
  private var queuedUrl: String? = null
  private var errorDrawable: Drawable? = null

  init {
    context.theme.obtainStyledAttributes(
        attributeSet,
        R.styleable.RoundCornerImageView,
        0,
        0).apply {
      try {
        cornerRadiusPx = getDimension(R.styleable.RoundCornerImageView_cornerRadius, DEFAULT_RADIUS_PX)
      } finally {
        recycle()
      }
    }
  }

  fun loadImage(url: String?, @DrawableRes errorRes: Int) {
    if (url == null) {
      cancelRequest()
    } else {
      queuedUrl = url
      errorDrawable = context.getDrawable(errorRes)
      loadImageInternal()
    }
  }

  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) = loadImageInternal(w, h)

  fun cancelRequest() {
    picasso.cancelRequest(this)
    cleanup()
  }

  override fun onDraw(canvas: Canvas) {
    drawable?.apply {
      setBounds(0, 0, width, height)
      draw(canvas)
    }
  }

  override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
    drawable = placeHolderDrawable
  }

  override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom) {
    val roundedDrawable = RoundedBitmapDrawableFactory.create(resources, bitmap)
    layoutParams.apply {
      width = bitmap.width
      height = bitmap.height
    }
    layoutParams = layoutParams
    roundedDrawable.cornerRadius = cornerRadiusPx
    drawable = roundedDrawable
    cleanup()
  }

  override fun onBitmapFailed(e: Exception, ignoredErrorDrawable: Drawable?) {
    layoutParams.apply {
      width = Math.min(width, height)
      height = width
    }
    drawable = errorDrawable
    cleanup()
  }

  private fun loadImageInternal(w: Int = width, h: Int = height) {
    if (w < 1 && h < 1) {
      return
    }
    if (queuedUrl != null) {
      picasso.load(queuedUrl)
          .noFade()
          .noPlaceholder()
          .centerCrop()
          .resize(w, h)
          .into(this)
    }
  }

  private fun cleanup() {
    queuedUrl = null
    errorDrawable = null
    drawable = null
  }

  private companion object {
    const val DEFAULT_RADIUS_PX = 25f
  }
}
