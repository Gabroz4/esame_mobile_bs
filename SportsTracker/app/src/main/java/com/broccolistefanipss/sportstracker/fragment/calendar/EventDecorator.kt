package com.broccolistefanipss.sportstracker.fragment.calendar

import android.content.Context
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.core.content.ContextCompat
import com.broccolistefanipss.sportstracker.R

//TODO: errore potrebbe essere nel formato della data

class EventDecorator(context: Context, private val dates: Set<CalendarDay>) : DayViewDecorator {

    private val drawable: Drawable = ContextCompat.getDrawable(context, R.drawable.circle_drawable)!!

    override fun shouldDecorate(day: CalendarDay): Boolean {
        val shouldDecorate = dates.contains(day)
        Log.d("EventDecorator", "Should decorate ${day}: $shouldDecorate")
        return shouldDecorate
    }


    override fun decorate(view: DayViewFacade) {
        Log.d("EventDecorator", "Decorating day: $view")
        view.setBackgroundDrawable(drawable)
    }
}
