package com.victoria.customer.ui.authentication.adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import com.victoria.customer.R
import com.victoria.customer.model.CountryData
import com.victoria.customer.util.CountryUtils
import kotlinx.android.synthetic.main.row_country_code_selection.view.*
import java.util.*


class CountryCodeAdapter(getContext: Context, private var dataList: ArrayList<CountryData>, private val getCountryCode: GetCountryCode)
    : ArrayAdapter<CountryData>(getContext, 0, dataList), Filterable {


     var orig: ArrayList<CountryData> =dataList
    private val mFilter = ItemFilter()

    internal var s: String? = null

    lateinit var view:View


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        view= LayoutInflater.from(context).inflate(R.layout.row_country_code_selection, parent, false)

        val holder = CountryHolder(view)
        holder.bind(position)

        return view
    }

    private inner class ItemFilter : Filter() {
        override fun performFiltering(constraint: CharSequence): Filter.FilterResults {
            val filterString = constraint.toString().toLowerCase()
            val results = Filter.FilterResults()

            val count = orig.size
            val tempItems = ArrayList<CountryData>(count)


            for (i in 0 until count) {
                if (orig[i].e164Cc.toLowerCase().contains(filterString)) {
                    tempItems.add(orig[i])
                } else if (orig[i].name.toLowerCase().contains(filterString)) {
                    tempItems.add(orig[i])
                }
            }

            results.values = tempItems
            results.count = tempItems.size

            return results
        }

        override fun publishResults(constraint: CharSequence, results: Filter.FilterResults) {
            dataList = results.values as ArrayList<CountryData>
            notifyDataSetChanged()
        }
    }

    override fun getFilter(): Filter {
        return mFilter
    }

    override fun getCount(): Int {
        return dataList.size
    }


    interface GetCountryCode {
        fun onSelectCountryCode(countrycode: String?, country: String?, id: CountryData?)
    }


    inner class CountryHolder(view: View) {

        fun bind(position: Int) {

            view.txtCountryname.text = dataList[position].name
            view.txtCountryCode.text = dataList[position].e164Cc


            view.imgCountry.setImageResource(CountryUtils.getFlagDrawableResId(dataList[position]))


            view.setOnClickListener { getCountryCode.onSelectCountryCode(dataList[position].e164Cc, dataList[position].name, dataList?.get(position)) }
        }
    }



}
