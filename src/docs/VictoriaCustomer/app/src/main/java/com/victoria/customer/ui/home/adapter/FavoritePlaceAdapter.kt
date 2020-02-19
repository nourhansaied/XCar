package com.victoria.customer.ui.home.adapter


import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.victoria.customer.R
import com.victoria.customer.core.Common
import com.victoria.customer.data.pojo.FavouriteAddress
import com.victoria.customer.model.AddressType
import kotlinx.android.synthetic.main.row_favorite_place_list_item.view.*


/**
 * Created  on 3/10/16.
 */

class FavoritePlaceAdapter(private val context: Context, private var eList: MutableList<FavouriteAddress>,var callbackFavPlace:CallbackFavPlace) : RecyclerView.Adapter<FavoritePlaceAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = View.inflate(context, R.layout.row_favorite_place_list_item, null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoritePlaceAdapter.ViewHolder, position: Int) {


        if (eList[position].type == Common.AddressType.OTHER.addressType) {

            holder.itemView.textViewLabel.visibility = View.GONE

        } else {
            holder.itemView.textViewLabel.visibility = View.VISIBLE
            holder.itemView.textViewLabel.text=eList[position].type

        }

        holder.itemView.textviewAddress.text = eList[position].address

        holder.itemView.imageViewDelete1.setOnClickListener {

            callbackFavPlace.deleteFavouriteAddress(eList[position])
            eList.removeAt(position)
            notifyItemRemoved(position)
        }

        holder.itemView.linearLayout.setOnClickListener {

            callbackFavPlace.selectedFavouriteAddress(eList[position])
        }
        //notifyItemRemoved(position)}

        //holder.bind(eList[position])
    }


    override fun getItemCount(): Int {
        return eList.size
    }


    public inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(value: AddressType) {


        }
    }

    interface CallbackFavPlace{

        fun deleteFavouriteAddress(item:FavouriteAddress)

        fun selectedFavouriteAddress(item: FavouriteAddress)
    }
}
