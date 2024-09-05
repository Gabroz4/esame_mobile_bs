package com.broccolistefanipss.sportstracker.fragment.calendar

import android.content.Context
import androidx.core.content.ContextCompat
import com.broccolistefanipss.sportstracker.R
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.spans.DotSpan


// TODO: aggiungere decorazioni tipo un pallino ove nel db ci sono giorni con CalendarTrainings per quello user
//  impostare giorno odierno all'apertura
//  grigiare giorni precedenti a quello corrente

class EventDecorator(private val context: Context, private val dates: Set<CalendarDay>) : DayViewDecorator {

    override fun shouldDecorate(day: CalendarDay): Boolean {
        // Decorare solo i giorni che sono nella lista degli allenamenti
        return dates.contains(day)
    }

    override fun decorate(view: DayViewFacade) {
        // Aggiungi un punto sotto il giorno con un colore specifico
        view.addSpan(DotSpan(8f, ContextCompat.getColor(context, com.google.android.material.R.color.material_blue_grey_800)))  // Personalizza il colore e la dimensione del punto
    }
}