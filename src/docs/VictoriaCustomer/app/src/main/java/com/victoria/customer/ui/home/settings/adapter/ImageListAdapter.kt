package com.victoria.customer.ui.home.settings.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.squareup.picasso.Picasso
import com.victoria.customer.R
import com.victoria.customer.ui.interfaces.OnItemClickListener
import com.victoria.customer.util.CircleTransformPicasso
import java.io.File


class ImageListAdapter(items: ArrayList<File>, internal var context: Context,internal var callBackInterface: OnItemClickListener<Int>) : RecyclerView.Adapter<ImageListAdapter.ViewHolder>() {

    private var items = ArrayList<File>()


    init {
        this.items = items
    }


    override fun getItemCount(): Int {
        return  1+items.size
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {

        val view: View = LayoutInflater.from(viewGroup.context).inflate(R.layout.row_contact_us_image, viewGroup, false)
        return ViewHolder(view)


    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(items, position,callBackInterface)
    }


    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        private var imageViewReviewImages1: ImageView = v.findViewById(R.id.imageViewAddPhoto)
        fun bind(list: ArrayList<File>, position: Int, onItemClickListener: OnItemClickListener<Int>) {

            if (position == list.size) {

                imageViewReviewImages1.setImageResource(R.drawable.addphoto)

            } else {
                Picasso.get()
                        .load(list[position])
                        .transform(CircleTransformPicasso())
                        .into(imageViewReviewImages1)
            }
            itemView.setOnClickListener {
                onItemClickListener.onItemClicked(adapterPosition)
            }
        }

    }

}
