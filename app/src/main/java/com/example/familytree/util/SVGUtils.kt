package com.example.familytree.util

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.PictureDrawable
import android.widget.ImageView
import com.caverock.androidsvg.SVG
import java.io.IOException

object SVGUtils {
    fun loadSvgFromAssets(context: Context, fileName: String): Drawable? {
        return try {
            val inputStream = context.assets.open(fileName)
            val svg = SVG.getFromInputStream(inputStream)
            val picture = svg.renderToPicture()
            PictureDrawable(picture)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
    
    fun loadSvgIntoImageView(context: Context, imageView: ImageView, fileName: String) {
        val drawable = loadSvgFromAssets(context, fileName)
        imageView.setImageDrawable(drawable)
    }
} 