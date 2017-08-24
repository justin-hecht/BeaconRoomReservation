package iosb.fraunhofer.de.baeconroomreservation.adapters;

import com.alamkanak.weekview.WeekViewEvent;

import java.util.Calendar;

import iosb.fraunhofer.de.baeconroomreservation.entity.Term;

/**
 *
 * Created by sakovi on 24.08.2017.
 */

public class Event extends WeekViewEvent
{
    private Term term;

    public Term getTerm() {
        return term;
    }

    public void setTerm(Term term) {
        this.term = term;
    }

    public Event(Term term) {
        this.term = term;
    }

    public Event(long id, String name, int startYear, int startMonth, int startDay, int startHour, int startMinute, int endYear, int endMonth, int endDay, int endHour, int endMinute, Term term) {
        super(id, name, startYear, startMonth, startDay, startHour, startMinute, endYear, endMonth, endDay, endHour, endMinute);
        this.term = term;
    }

    public Event(long id, String name, String location, Calendar startTime, Calendar endTime, Term term) {
        super(id, name, location, startTime, endTime);
        this.term = term;
    }

    public Event(long id, String name, Calendar startTime, Calendar endTime, Term term) {
        super(id, name, startTime, endTime);
        this.term = term;
    }
}
