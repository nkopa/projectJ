package pl.project.web;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.enterprise.context.SessionScoped;
//import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.validator.ValidatorException;
//import javax.faces.bean.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import pl.project.domain.JCalendar;
import pl.project.domain.JUser;
import pl.project.service.CalendarManager;
import pl.project.service.UserManager;

@SessionScoped
@Named("userBean")
public class UserBean implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private UserManager userManager;
	
	@Inject
	private CalendarManager calendarManager;
	
	private String loggedUser = null;
	
	private JUser user = new JUser();
	private String message;

	//****************************************
	
	public String logout(){
		loggedUser = null;
		return "home?faces-redirect=true";
	}
	
	public String login(){
		if(userManager.confirmUser(user.getUsername())){
			String password = user.getPassword();
			JUser founduser = userManager.findUserByName(user.getUsername());
			
			if(userManager.confirmPassword(password, founduser)){
				loggedUser = user.getUsername();
				user = new JUser();
				return "home?faces-redirect=true";
			}
		}
		message = "Nie można zalogować ponieważ podany login lub hasło nie jest prawidłowe.";
		return "error?faces-redirect=true";
	}
	
	public String register(){
		if(!userManager.confirmUser(user.getUsername())){
			JCalendar calendar = new JCalendar();
			calendar = calendarManager.createCalendar(calendar);
			user.setCalendar(calendar);
			
			user = userManager.createUser(user);
		
			loggedUser = user.getUsername();
			user = new JUser();
			return "home?faces-redirect=true";
		}
		message = "Nie można zarejestrować ponieważ podany login już istnieje w bazie.";
		return "error?faces-redirect=true";
	}
	
	//****************************************
	
	public boolean isLogged(){
		if(loggedUser == null) return false;
		else return true;
	}
	
	public String canSeeCalendar(){
		if(isLogged()) return "true";
		return "false";
	}
	
	public String canAddEvent(){
		if(isLogged()) return "true";
		return "false";
	}
	
	// walidacja
	public void uniqueLoginCheck(FacesContext context, UIComponent component, Object value){
		String login = (String) value;
		
		if(userManager.confirmUser(login)){
			FacesMessage message = new FacesMessage("Podany login już istnieje w bazie.");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(message);
		}
	}
	
	public void canLoginCheck(ComponentSystemEvent event){
		UIForm form = (UIForm) event.getComponent();
		UIInput login = (UIInput) form.findComponent("login");
		UIInput password = (UIInput) form.findComponent("password");
		
		String loginString = (String) login.getValue();
		String passwordString = (String) password.getValue();
		
		if(userManager.confirmUser(loginString)){
			JUser founduser = userManager.findUserByName(loginString);
			if(!userManager.confirmPassword(passwordString, founduser)){
				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage(form.getClientId(), new FacesMessage("Nie można zalogować ponieważ podany login lub hasło nie jest prawidłowe."));
				context.renderResponse();
			}
		}
		else {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(form.getClientId(), new FacesMessage("Nie można zalogować ponieważ podany login lub hasło nie jest prawidłowe."));
			context.renderResponse();
		}
	}
	
	public void passwordCheck(FacesContext context, UIComponent component, Object value){
		String password = (String) value;
		int whitespace = 0;
		int digit = 0;
		int smallLetter = 0;
		int bigLetter = 0;
		
		for(int i=0;i<password.length();i++){
			if(Character.isWhitespace(password.charAt(i))) whitespace++;
			if(Character.isDigit(password.charAt(i))) digit++;
			if(Character.isWhitespace(password.charAt(i))) whitespace++;
			if(Character.isLowerCase(password.charAt(i))) smallLetter++;
			if(Character.isUpperCase(password.charAt(i))) bigLetter++;
		}
		
		if(whitespace != 0){
			FacesMessage message = new FacesMessage("Hasło nie może zawierać białych znaków.");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(message);
		}
		
		if(digit == 0){
			FacesMessage message = new FacesMessage("Hasło musi zawierać co najmniej 1 cyfrę.");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(message);
		}
		
		if(smallLetter == 0){
			FacesMessage message = new FacesMessage("Hasło musi zawierać co najmniej 1 małą literę.");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(message);
		}
		
		if(bigLetter == 0){
			FacesMessage message = new FacesMessage("Hasło musi zawierać co najmniej 1 wielką literę.");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(message);
		}
		
	}
	
	//****************************************
	
	public UserManager getUserManager() {
		return userManager;
	}
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public String getLoggedUser() {
		return loggedUser;
	}

	public void setLoggedUser(String loggedUser) {
		this.loggedUser = loggedUser;
	}

	public JUser getUser() {
		return user;
	}

	public void setUser(JUser user) {
		this.user = user;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public CalendarManager getCalendarManager() {
		return calendarManager;
	}

	public void setCalendarManager(CalendarManager calendarManager) {
		this.calendarManager = calendarManager;
	}
}
