package app

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.util.AttributeSet
import android.view.View
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import org.stoyicker.dinger.R

internal class RoundedImageView(context: Context, attributeSet: AttributeSet? = null)
  : View(context, attributeSet), Target {
  private val cornerRadiusPx: Float
  private val picasso = Picasso.get()
  private var drawable: Drawable? = null
    set(value) {
      field = value
      postInvalidate()
    }
  private var queuedUrl: String? = null

  init {
    context.theme.obtainStyledAttributes(
        attributeSet,
        R.styleable.RoundedImageView,
        0,
        0).apply {
      try {
        cornerRadiusPx = getDimension(R.styleable.RoundedImageView_cornerRadius, DEFAULT_RADIUS_PX)
      } finally {
        recycle()
      }
    }
  }

  fun loadImage(url: String) {
    queuedUrl = url
    loadImageInternal()
  }

  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) = loadImageInternal(w, h)

  fun cancelRequest() {
    picasso.cancelRequest(this)
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
  }

  override fun onBitmapFailed(e: Exception, errorDrawable: Drawable) {
    drawable = errorDrawable
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
          .error(ColorDrawable(Color.RED))
          .into(this)
    }
  }

  private companion object {
    const val DEFAULT_RADIUS_PX = 25f
  }
}
