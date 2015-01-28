package pl.project.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import pl.project.domain.JCalendar;
import pl.project.domain.JEvent;
import pl.project.domain.JUser;

@Stateless
public class CalendarManager {

	@PersistenceContext
	EntityManager em;
	
	public JCalendar getCalendar(JUser user) {
		//return em.find(JCalendar.class, 1);
		return user.getCalendar();
	}

	public JCalendar createCalendar(JCalendar calendar) {
		calendar.setId(null);
		//calendar.setEventList(new ArrayList<JEvent>());
		em.persist(calendar);
		return calendar;
	}

	public List<JEvent> getEvents(JCalendar calendar) {
		JCalendar foundCalendar = findCalendarById(calendar.getId());
		List<JEvent> events = foundCalendar.getEventList();
		if(events == null) return new ArrayList<JEvent>();
		return events;
	}

	public void addEvent(JEvent event, JCalendar calendar) {
		event.setId(null);
		em.persist(event);
		
		JCalendar foundCalendar = findCalendarById(calendar.getId());
		
		//event.setId(null);
		foundCalendar.getEventList().add(event);
		
		em.merge(foundCalendar);
	}

	public void deleteEvent(JEvent event, JCalendar calendar) {
		JCalendar foundCalendar = findCalendarById(calendar.getId());
		
		int i = 0;
		List<JEvent> events = foundCalendar.getEventList();
		while(i < events.size() && event.getId() != events.get(i).getId()) i++;
		
		if(i < events.size()) events.remove(i);//usuwa zdarzenie na pozycji i jesli miesci sie w rozmiarze
		
		foundCalendar.setEventList(events);
		em.merge(foundCalendar);
	}

	public void editEvent(JEvent event, JCalendar calendar) {
		JCalendar foundCalendar = findCalendarById(calendar.getId());
		
		int i = 0;
		List<JEvent> events = foundCalendar.getEventList();
		while(i < events.size() && event.getId() != events.get(i).getId()) i++;
		
		if(i < events.size()){
			events.get(i).setName(event.getName());
			events.get(i).setPlace(event.getPlace());
			events.get(i).setDescription(event.getDescription());
			events.get(i).setDate(event.getDate());
			
			foundCalendar.setEventList(events);
			em.merge(foundCalendar);
		}
	}

	public JCalendar findCalendarById(Long id) {
		return em.find(JCalendar.class, id);
	}
	
	public JEvent getEventById(Long id){
		return em.find(JEvent.class, id);
	}
	
	@SuppressWarnings("unchecked")
	public List<JCalendar> getAllCalendars(){
		return em.createNamedQuery("calendar.all").getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<JEvent> getAllEvents(){
		return em.createNamedQuery("event.all").getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<JEvent> getEventsAfter(JCalendar calendar, Date datePoint){
		return em.createNamedQuery("event.afterDate").setParameter("idCalendar", calendar.getId()).setParameter("datePoint", datePoint).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<JEvent> findByName(JCalendar calendar, Date datePoint, String name){
		return em.createNamedQuery("event.likeName").setParameter("idCalendar", calendar.getId()).setParameter("datePoint", datePoint).setParameter("name", "%"+name.toUpperCase()+"%").getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<JEvent> findByPlace(JCalendar calendar, Date datePoint, String place){
		return em.createNamedQuery("event.likePlace").setParameter("idCalendar", calendar.getId()).setParameter("datePoint", datePoint).setParameter("place", "%"+place.toUpperCase()+"%").getResultList();
	}

}
