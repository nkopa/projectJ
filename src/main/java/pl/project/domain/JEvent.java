package pl.project.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

@Entity
@NamedQueries({ 
	@NamedQuery(name = "event.all", query = "Select e from JEvent e"),
	@NamedQuery(name = "event.afterDate", query = "Select e from JCalendar c JOIN c.eventList e Where c.id=:idCalendar AND e.date>:datePoint Order by e.date"),
	@NamedQuery(name = "event.likeName", query = "Select e from JCalendar c JOIN c.eventList e Where c.id=:idCalendar AND e.date>:datePoint AND UPPER(e.name) Like :name Order by e.date"),
	@NamedQuery(name = "event.likePlace", query = "Select e from JCalendar c JOIN c.eventList e Where c.id=:idCalendar AND e.date>:datePoint AND UPPER(e.place) Like :place Order by e.date")
})
public class JEvent {
	private Long id;
	private String name;
	private String description;
	private String place;
	private Date date;
	
	//******************************
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Size(min = 2, max = 30)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Size(min = 0, max = 200)
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Size(min = 2, max = 30)
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}
	
	@Temporal(TemporalType.DATE)
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
}
