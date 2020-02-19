package com.victoria.customer.ui.home.settings.adapter


import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import com.victoria.customer.R
import com.victoria.customer.data.pojo.CardListing
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.row_card_item_layout.*


/**
 * Created  on 3/10/16.
 */

class PaymentDetailsAdapter(private val context: Context?, private var eList: List<CardListing>, var callBackInterFace: CallBackInterFace) : RecyclerView.Adapter<PaymentDetailsAdapter.ViewHolder>() {
    lateinit var view: View
    var selectedPosition = -1
    var deleteVisible = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        view = LayoutInflater.from(context).inflate(R.layout.row_card_item_layout, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.checkBoxSelect.isChecked = position == selectedPosition
        if (deleteVisible) {
            holder.imageViewDelete1.visibility = View.VISIBLE
            holder.checkBoxSelect.visibility = View.GONE
        } else {
            holder.imageViewDelete1.visibility = View.GONE
            holder.checkBoxSelect.visibility = View.VISIBLE
        }

        holder.checkBoxSelect.setOnClickListener {
            selectedPosition = position
            notifyDataSetChanged()
        }

        holder.imageViewDelete1.setOnClickListener {
            callBackInterFace.deleteClick(eList[position])
        }

        Picasso.get()
                .load(eList[position].cardImage)
                .into(holder.imageViewCard)
        holder.textViewCardNumber.text = eList[position].cardNumber.toString()
        holder.textViewExpiryDate.text = eList[position].expiryDate.toString()
    }


    override fun getItemCount(): Int {
        return eList.size
    }


    inner class ViewHolder(override val containerView: View?) : RecyclerView.ViewHolder(view), LayoutContainer {
        fun bind(service: String) {

        }
    }

    fun showHide(isDeleteVisible: Boolean) {
        deleteVisible = isDeleteVisible
    }

    interface CallBackInterFace {
        fun deleteClick(cardDetail: CardListing)
    }
}
