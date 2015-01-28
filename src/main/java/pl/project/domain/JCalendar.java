package pl.project.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

@Entity
@NamedQueries({ 
	@NamedQuery(name = "calendar.all", query = "Select c from JCalendar c")
})
public class JCalendar {
	private Long id;
	
	private List<JEvent> eventList;

	//**********************************
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@OneToMany(fetch = FetchType.LAZY)
	//@OrderBy("date DESC")
	public List<JEvent> getEventList() {
		return eventList;
	}

	public void setEventList(List<JEvent> eventList) {
		this.eventList = eventList;
	}
	
}
