package com.victoria.customer.ui.home.adapter


import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.victoria.customer.R
import kotlinx.android.synthetic.main.row_transaction_history_item.view.*


/**
 * Created  on 3/10/16.
 */

class TransactionHistoryAdapter(private val context: Context?, private var eList: List<String>): RecyclerView.Adapter<TransactionHistoryAdapter.ViewHolder>() {
    lateinit var view: View

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        view = LayoutInflater.from(context).inflate(R.layout.row_transaction_history_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionHistoryAdapter.ViewHolder, position: Int) {


        if(position%2==0){
            view.textViewTransactionDate.visibility=View.GONE

        }else{

            view.textViewTransactionDate.visibility=View.VISIBLE
        }


    }


    override fun getItemCount(): Int {
        return 5
    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(service: String,position: Int) {


        }
    }
}
