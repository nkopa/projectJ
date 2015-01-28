package pl.project.service;

import java.util.Date;
import java.util.List;

import pl.project.domain.JCalendar;
import pl.project.domain.JEvent;
import pl.project.domain.JUser;

public interface CalendarInterface {
	JCalendar getCalendar(JUser user);
	JCalendar findCalendarById(Long id);
	JCalendar createCalendar(JCalendar calendar);
	List<JCalendar> getAllCalendars();
	
	List<JEvent> getEvents(JCalendar calendar);
	List<JEvent> getEventsAfter(JCalendar calendar, Date datePoint);
	List<JEvent> findByName(JCalendar calendar, Date datePoint, String name);
	List<JEvent> findByPlace(JCalendar calendar, Date datePoint, String place);
	JEvent getEventById(Long id);
	List<JEvent> getAllEvents();
	void addEvent(JEvent event, JCalendar calendar);
	void deleteEvent(JEvent event, JCalendar calendar);
	void editEvent(JEvent event, JCalendar calendar);
}
