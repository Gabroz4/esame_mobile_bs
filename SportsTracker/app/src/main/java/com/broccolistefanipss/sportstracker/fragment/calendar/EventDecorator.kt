package com.broccolistefanipss.sportstracker.fragment.calendar

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.Log
import androidx.core.content.ContextCompat
import com.broccolistefanipss.sportstracker.R
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.spans.DotSpan

//class EventDecorator(context: Context, private val dates: Set<CalendarDay>) : DayViewDecorator {
//    private val drawable: GradientDrawable = ContextCompat.getDrawable(context, R.drawable.circle_drawable) as GradientDrawable
//
//    override fun shouldDecorate(day: CalendarDay): Boolean {
//        return dates.contains(day)
//    }
//
//    override fun decorate(view: DayViewFacade) {
//        Log.d("EventDecorator", "Decorating day: $view")
//        view.addSpan(DotSpan(5f, R.color.red))
//    }
//}
