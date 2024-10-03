package com.example.simonsays

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView

class ImageAdapter(val context: Context) : BaseAdapter() {

    private val images = intArrayOf(R.drawable.blue, R.drawable.red, R.drawable.yellow, R.drawable.green)
    private val images2 = intArrayOf(R.drawable.blue2, R.drawable.red2, R.drawable.yellow2, R.drawable.green2)

    private val index = intArrayOf(0, 1, 2, 3)
    private val shuffledIndex = index.clone().apply { shuffle() }

    private val shuffledImages = IntArray(images.size) { i -> images[ shuffledIndex [i] ] }
    private val shuffledImages2 = IntArray(images2.size) { i -> images2[ shuffledIndex [i] ] }

    override fun getCount(): Int {
        return shuffledImages.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var gridView: View? = convertView
        if (gridView == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            gridView = inflater.inflate(R.layout.custom_image, parent, false)
        }

        val imagesView = gridView?.findViewById<ImageView>(R.id.img)
        imagesView?.setImageResource(shuffledImages[position])
        return gridView!!
    }

    override fun getItem(position: Int): Any {
        return shuffledImages[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun updateImage(position: Int) {
        var changedImage = shuffledImages[position]
        shuffledImages[position] = shuffledImages2[position]
        notifyDataSetChanged()
        Handler(Looper.getMainLooper()).postDelayed({
            shuffledImages[position] = changedImage
            notifyDataSetChanged()
        }, 1000)
    }

}
