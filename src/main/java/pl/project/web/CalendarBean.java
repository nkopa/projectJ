package pl.project.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.model.ListDataModel;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;

import pl.project.domain.JCalendar;
import pl.project.domain.JEvent;
import pl.project.domain.JUser;
import pl.project.service.CalendarManager;
import pl.project.service.UserManager;

@SessionScoped
@Named("calendarBean")
public class CalendarBean implements Serializable{
	private static final long serialVersionUID = 1L;

	@Inject
	private CalendarManager calendarManager;
	
	@Inject
	private UserManager userManager;
	
	@Inject
	private UserBean userBean;
	
	private ListDataModel<JEvent> events = new ListDataModel<JEvent>();
	private JEvent eventToShow;
	private JEvent eventToAdd = new JEvent();
	private String searchField;
	private int searchOption = 0; //0-normal, 1-name, 2-place
	
	//********************************************************
	
	public String showEvent(){
		eventToShow = calendarManager.getEventById(events.getRowData().getId());
		return "event";
	}
	
	public String addEvent(){
		calendarManager.addEvent(eventToAdd, getCalendar());
		eventToAdd = new JEvent();
		return "calendar?faces-redirect=true";
	}
	
	public String editEvent(){
		calendarManager.editEvent(eventToShow, getCalendar());
		return "event?faces-redirect=true";
	}
	
	public String deleteEvent(){
		calendarManager.deleteEvent(events.getRowData(), getCalendar());
		return "calendar?faces-redirect=true";
	}
	
	public String findByName(){
		searchOption = 1;
		return "calendar";
	}
	
	public String findByPlace(){
		searchOption = 2;
		return "calendar";
	}
	
	public String findByNormal(){
		searchOption = 0;
		return "calendar";
	}
	
	//********************************************************
	
	private JCalendar getCalendar(){
		JUser user = userManager.findUserByName(userBean.getLoggedUser());
		return calendarManager.getCalendar(user);
	}
	
	// walidacja
	public void dateCheck(FacesContext context, UIComponent component, Object value){
		Date dateToCheck = (Date) value;
		//Date today = new Date();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		
		if(dateToCheck.compareTo(cal.getTime()) < 0){
			FacesMessage message = new FacesMessage("Podana data jest datą z przeszłości. Podaj późniejszą datę.");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(message);
		}
	}
	
	//********************************************************
	
	public CalendarManager getCalendarManager() {
		return calendarManager;
	}

	public void setCalendarManager(CalendarManager calendarManager) {
		this.calendarManager = calendarManager;
	}

	public ListDataModel<JEvent> getEvents() {
		List<JEvent> foundEvents;
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		switch(searchOption){
		case 1:
			foundEvents = calendarManager.findByName(getCalendar(), cal.getTime(), searchField);
			break;
			
		case 2:
			foundEvents = calendarManager.findByPlace(getCalendar(), cal.getTime(), searchField);
			break;
			
		default:
			foundEvents = calendarManager.getEventsAfter(getCalendar(), cal.getTime());
			break;
		}
		//Calendar cal = Calendar.getInstance();
		//cal.add(Calendar.DATE, -1);
		//List<JEvent> foundEvents = calendarManager.getEventsAfter(getCalendar(), cal.getTime());//calendarManager.getEvents(getCalendar());
		/*for(int i=foundEvents.size()-1; i>=0; i--){
			JEvent event = foundEvents.get(i);
			if(event.getDate().compareTo(today) < 0){ //odfiltrowanie wcześniejszych zdarzeń
				foundEvents.remove(i);
			}
		}
		
		Collections.sort(foundEvents, new Comparator<JEvent>() {
			@Override
			public int compare(JEvent e1, JEvent e2) {
 
				return e1.getDate().compareTo(e2.getDate());
 
			}
		});*/
		
		events.setWrappedData(foundEvents);
		return events;
	}

	public void setEvents(ListDataModel<JEvent> events) {
		this.events = events;
	}

	public UserBean getUserBean() {
		return userBean;
	}

	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}

	public UserManager getUserManager() {
		return userManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public JEvent getEventToShow() {
		return eventToShow;
	}

	public void setEventToShow(JEvent eventToShow) {
		this.eventToShow = eventToShow;
	}

	public JEvent getEventToAdd() {
		return eventToAdd;
	}

	public void setEventToAdd(JEvent eventToAdd) {
		this.eventToAdd = eventToAdd;
	}

	public String getSearchField() {
		return searchField;
	}

	public void setSearchField(String searchField) {
		this.searchField = searchField;
	}
}
