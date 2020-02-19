package com.victoria.driver.ui.home.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.squareup.picasso.Picasso
import com.victoria.driver.R
import com.victoria.driver.ui.interfaces.OnItemClickListener
import com.victoria.driver.util.CircleTransform
import java.io.File


class ImageListAdapter(items: ArrayList<File>, var context: Context, internal var callBackInterface: OnItemClickListener<Int>) : RecyclerView.Adapter<ImageListAdapter.ViewHolder>() {

    private var items = ArrayList<File>()

    private val USER = 0
    private val IMAGE = 1


    init {
        this.items = items
    }


    override fun getItemId(position: Int): Long {
        return super.getItemId(position)

    }

    override fun getItemCount(): Int {
        return 1 + items.size
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {

        val view: View = LayoutInflater.from(viewGroup.context).inflate(R.layout.row_contact_us_image, viewGroup, false)
        return ViewHolder(view)


    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(items, position, callBackInterface, context)

    }


    public class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        var imageViewReviewImages1: ImageView = v.findViewById(R.id.imageViewAddPhoto)
        fun bind(list: ArrayList<File>, position: Int, onItemClickListener: OnItemClickListener<Int>, context: Context) {

            if (position == list.size) {

                imageViewReviewImages1.setImageResource(R.drawable.addphoto)

            } else {
                Picasso.get()
                        .load(list[position])
                        .transform(CircleTransform())
                        .into(imageViewReviewImages1)
            }
            itemView.setOnClickListener {
                onItemClickListener.onItemClicked(adapterPosition)
            }
        }

    }


    interface CallBackInterface {
        fun imageCallBack()
    }
}
