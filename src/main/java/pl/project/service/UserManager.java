package pl.project.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

//import pl.project.domain.JCalendar;
//import pl.project.domain.JEvent;
import pl.project.domain.JUser;

@Stateless
public class UserManager {

	@PersistenceContext
	EntityManager em;
	
	public JUser createUser(JUser user) {
		/*JCalendar calendar = new JCalendar();
		calendar.setId(null);
		calendar.setEventList(new ArrayList<JEvent>());*/

		//user.setCalendar(calendar);
		user.setId(null);
		em.persist(user);
		return user;
	}

	public boolean confirmPassword(String password, JUser user) {
		JUser foundUser = em.find(JUser.class, user.getId());
		if(password.equals(foundUser.getPassword())) return true;
		return false;
	}

	public boolean confirmUser(String username) {
		@SuppressWarnings("unchecked")
		List<JUser> users = em.createNamedQuery("user.all").getResultList();
		for(JUser user : users){
			if(username.equals(user.getUsername())) return true;
		}
		return false;
	}

	public JUser findUserByName(String username) {
		@SuppressWarnings("unchecked")
		List<JUser> users = em.createNamedQuery("user.getByName").setParameter("username", username).getResultList();
		
		if(users == null || users.isEmpty()) return null;
		if(users.size() != 1) return null;
		
		return users.get(0);
	}

	public JUser getUserById(Long id) {
		//JUser user = em.find(JUser.class, id);
		return em.find(JUser.class, id);
	}

}
