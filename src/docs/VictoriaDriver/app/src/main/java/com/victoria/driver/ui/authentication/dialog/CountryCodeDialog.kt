package com.victoria.driver.ui.authentication.dialog


import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import com.victoria.driver.R
import com.victoria.driver.ui.authentication.adapter.CountryCodeAdapter
import com.victoria.driver.ui.interfaces.ISelectCountry
import com.victoria.driver.ui.model.CountryData
import com.victoria.driver.util.CountryUtils
import kotlinx.android.synthetic.main.dialog_country_code_selection.*


class CountryCodeDialog : DialogFragment(), CountryCodeAdapter.GetCountryCode {

    private var adapter: CountryCodeAdapter? = null

    private var callback: ISelectCountry? = null

    fun setCallback(callback: ISelectCountry) {
        this.callback = callback
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_country_code_selection, container, false)
        val wmlp = dialog.window!!.attributes
        wmlp.gravity = Gravity.FILL_HORIZONTAL
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setData()

        imgClose.setOnClickListener {

            dismiss()
        }

        lstcountry.isTextFilterEnabled = true

        etAutocompleteTextView.addTextChangedListener(MyTextWatcher(adapter!!))


    }

    //set a data

    private fun setData() {

        adapter = CountryCodeAdapter(context!!, CountryUtils.readCountryJson(activity), this)

        lstcountry.adapter = adapter

    }

    override fun onSelectCountryCode(countrycode: String, country: String, image: CountryData) {
        callback?.selectCountry(countrycode, country, image)
        dismiss()
    }



    //search a country based on user typing

    inner class MyTextWatcher(private val lAdapter: CountryCodeAdapter) : TextWatcher {

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable) {
            lAdapter.filter.filter(s.toString().trim())
        }
    }

}
