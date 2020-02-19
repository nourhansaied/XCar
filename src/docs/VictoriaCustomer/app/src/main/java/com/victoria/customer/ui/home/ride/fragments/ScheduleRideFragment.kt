package com.victoria.customer.ui.home.ride.fragments

import android.arch.lifecycle.ViewModelProviders
import android.view.View
import android.widget.LinearLayout
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.victoria.customer.R
import com.victoria.customer.core.Common
import com.victoria.customer.core.Validator
import com.victoria.customer.data.URLFactory
import com.victoria.customer.di.VictoriaCustomer
import com.victoria.customer.di.component.FragmentComponent
import com.victoria.customer.exception.ApplicationException
import com.victoria.customer.ui.base.BaseFragment
import com.victoria.customer.ui.home.fragments.HomeStartFragment
import com.victoria.customer.ui.home.viewmodel.FareEstimationViewModel
import com.victoria.customer.util.DateTimeFormatter
import com.victoria.customer.util.MyCustomTimePickerDialog
import com.victoria.customer.util.OneDayDecorator
import kotlinx.android.synthetic.main.fragment_schedule_ride_layout.*
import kotlinx.android.synthetic.main.toolbar_with_close.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class ScheduleRideFragment : BaseFragment() {

    lateinit var tripDateAndTime: String
    var dateAndTimeToSend: String = ""

    @Inject
    lateinit var validator: Validator

    override fun createLayout(): Int {
        return R.layout.fragment_schedule_ride_layout
    }

    override fun inject(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    private val fareEstimationViewModel: FareEstimationViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[FareEstimationViewModel::class.java]
    }

    override fun bindData() {
        toolBarText.text = getString(R.string.toolbar_title_schedule_ride)

        observePlaceOrder()
        //setUpHorizontalCalendar()
        setupCalender()

        if (arguments != null && arguments!!.containsKey(Common.TOTAL_COST)) {
            textViewTotalEstiCost.text = arguments!!.getString(Common.TOTAL_COST)
        }
        buttonSubmitRequest.setOnClickListener(this::onViewClick)
        textViewSelectTime.setOnClickListener(this::onViewClick)
        imageViewBack.setOnClickListener(this::onViewClick)
    }

    override fun onViewClick(view: View) {
        super.onViewClick(view)
        when (view.id) {

            R.id.imageViewBack -> {
                navigator.goBack()

            }

            R.id.buttonSubmitRequest -> {
                VictoriaCustomer.getRideData().trip_type = Common.RideType.LATER.rideType
                if (textViewSelectTime.text.toString().trim().isNotEmpty()) {
                    placeOrderApi()
                } else {
                    showMessage(getString(R.string.validation_empty_time))
                }

                //navigator.load(CarAllocationFragment::class.java).replace(true)
                //navigator.load(HomeFragment::class.java).clearHistory().replace(false)
            }

            R.id.textViewSelectTime -> {
                val mcurrentTime = Calendar.getInstance()
                mcurrentTime.add(Calendar.HOUR_OF_DAY, +1)

                var mTimePicker: android.app.TimePickerDialog? = null
                val selectedTime = Calendar.getInstance()

                mTimePicker = MyCustomTimePickerDialog(context, { view, hourOfDay, minute1 ->
                    try {
                        selectedTime.set(materialCalender.selectedDate.year, materialCalender.selectedDate.month,
                                materialCalender.selectedDate.day)
                        selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        selectedTime.set(Calendar.MINUTE, minute1)

                        if (/*(mcurrentTime.get(Calendar.DAY_OF_YEAR) == selectedTime.get(Calendar.DAY_OF_YEAR)
                                    && mcurrentTime.get(Calendar.YEAR) == selectedTime.get(Calendar.YEAR))
                            &&*/ selectedTime.before(mcurrentTime)) {

                            throw ApplicationException("Please select time after " +
                                    (DateTimeFormatter.date(mcurrentTime.time).formatDateToLocalTimeZone("hh:mm a")) +
                                    " for today")
                        } else {

                            val startTime = String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute1)

                            textViewSelectTime.text = DateTimeFormatter.date(startTime, "HH:mm")
                                    .formatDateToLocalTimeZone("hh:mm aaa")
                            dateAndTimeToSend = DateTimeFormatter.dateTimeConvertLocalToServer(tripDateAndTime + " " + startTime + ":00",
                                    Common.DATE_TIME_FORMAT_UTC, Common.DATE_TIME_FORMAT_UTC)
                            //2019-05-05 22:25:00
                        }

                    } catch (e: ApplicationException) {
                        showMessage(e.message)
                    }
                }
                        , Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), false)

                mTimePicker.setOnCancelListener(null)
                mTimePicker.setTitle("Select Time")
                mTimePicker.show()
            }
        }
    }

    private fun checkValidation() {

        try {
            validator.submit(textViewSelectTime)
                    .checkEmpty()
                    .errorMessage(getString(R.string.validation_empty_time))
                    .check()
            placeOrderApi()

        } catch (e: ApplicationException) {
            hideKeyBoard()
            showSnackBar(e.message)
        }
    }
    /*private fun setUpHorizontalCalendar(){

        *//* starts before 1 month from now *//*
        val startDate = Calendar.getInstance()
        startDate.add(Calendar.MONTH, -1)

        *//* ends after 1 month from now *//*
        val endDate = Calendar.getInstance()
        endDate.add(Calendar.MONTH, 1)

         horizontalCalendar = HorizontalCalendar.Builder(activity!!, R.id.weekCalendar)
                .range( startDate, endDate)
                .datesNumberOnScreen(7)   // Number of Dates cells shown on screen (default to 5).
                .configure()    // starts configuration.
                    // default to "MMM".
                .formatMiddleText("dd")    // default to "dd".
                .formatBottomText("EEE")    // default to "EEE".
                .showTopText(false)              // show or hide TopText (default to true).
                .showBottomText(true)           // show or hide BottomText (default to true).
                .textColor(resources.getColor(R.color.gray), resources.getColor(R.color.gray))    // default to (Color.LTGRAY, Color.WHITE).
                .selectedDateBackground(resources.getDrawable(R.drawable.drawable_round_blue))      // set selected date cell background.
                .selectorColor(resources.getColor(R.color.white))               // set selection indicator bar's color (default to colorAccent).
                .end()          // ends configuration.
                .defaultSelectedDate(endDate)    // Date to be selected at start (default to current day `Calendar.getInstance()`).
                .build()


        horizontalCalendar.calendarListener = object : HorizontalCalendarListener() {
            override fun onDateSelected(date: Calendar, position: Int) {

            }

            override fun onCalendarScroll(calendarView: HorizontalCalendarView,
                                          dx: Int, dy: Int) {

            }

            override fun onDateLongClicked(date: Calendar, position: Int): Boolean {
                return true
            }
        }
        }*/


    private fun setupCalender() {

        val c = Calendar.getInstance()

        val maxDate = Calendar.getInstance()
        maxDate.add(Calendar.MONTH, 1)
        //CalendarDay.
        (materialCalender.getChildAt(0) as LinearLayout).getChildAt(0).visibility = View.GONE
        (materialCalender.getChildAt(0) as LinearLayout).getChildAt(2).visibility = View.GONE

        materialCalender.state().edit()
                .setCalendarDisplayMode(CalendarMode.WEEKS)
                .setFirstDayOfWeek(Calendar.MONDAY)
                .setMinimumDate(Calendar.getInstance())
                .setMaximumDate(maxDate.time)
                .commit()

        //materialCalendarView.state().edit().setMaximumDate(maxDate.getTimeInMillis()).commit();
        //        Calendar calendar = Calendar.getInstance();


        materialCalender.setSelectedDate(c)
        var dayDecor = OneDayDecorator()
        materialCalender.addDecorator(dayDecor)

        materialCalender.getChildAt(0).setBackgroundColor(context?.resources?.getColor(R.color.white)!!)

        tripDateAndTime = getDateTime()

        materialCalender.setOnDateChangedListener { widget, date, selected ->
            textViewSelectTime.text = ""
            dayDecor.setDate(date.date)
            materialCalender.addDecorator(dayDecor)
            tripDateAndTime = DateTimeFormatter.localToUtcConvert(DateTimeFormatter.date(date.date).formatDateToLocalTimeZone("dd MMM yyyy"), "dd MMM yyyy", "yyyy-MM-dd")
            //bookOrderReq.booking_date = DateTimeFormatter.date(date.date).formatDateToLocalTimeZone("dd MMM yyyy")

        }
        materialCalender.addDecorator(object : DayViewDecorator {
            override fun shouldDecorate(day: CalendarDay): Boolean {
                return true
            }


            override fun decorate(view: DayViewFacade) {
                //view.setSelectionDrawable(resources.getDrawable(R.drawable.cal_selector_))
            }
        })
    }


    private fun placeOrderApi() {
        VictoriaCustomer.getRideData().tripdatetime = dateAndTimeToSend
        fareEstimationViewModel.placeOrder(VictoriaCustomer.getRideData())
    }

    fun observePlaceOrder() {
        fareEstimationViewModel.placeOrderLiveData.observe(this@ScheduleRideFragment, onChange = {

            when (it.responseCode) {
                URLFactory.ResponseCode.INVALID_REQUEST_FAIL_REQUESt -> {
                    showMessage(it.message)
                }
                URLFactory.ResponseCode.SUCCESS -> {
                    showMessage(it.message)
                    //VictoriaCustomer.setTripData(it.data)
                    navigator.load(HomeStartFragment::class.java).replace(true)
                }
            }

        }, onError = {

            true
        })
    }

    private fun getDateTime(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val date = Date()
        return dateFormat.format(date)
    }
}
