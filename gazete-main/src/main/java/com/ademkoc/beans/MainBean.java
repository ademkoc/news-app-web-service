package com.ademkoc.beans;

import java.io.IOException;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@ManagedBean(name="main")
@SessionScoped
public class MainBean {

	private static boolean isLoginIn;
	public static final String KEY = "REGISTER_STATUS";
	
	
	public void onload() throws IOException {
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();

		if (!isLoginIn()) {
		    ec.getFlash().clear();
		    ec.redirect("index.jsf");
		    return;
		}
		
	    if(ec.getFlash().containsKey(KEY)) {
    		FacesContext facesContext = FacesContext.getCurrentInstance();
    		FacesMessage facesMessage = new FacesMessage();
	    	if((boolean) ec.getFlash().get(KEY)) {
	    		facesMessage.setSummary("200");
	    	}else{
	    		facesMessage.setSummary("400");
	    	}
    		facesContext.addMessage("formid-kaynaklar:kaynak", facesMessage);
	    }
	}


	public static boolean isLoginIn() {
		return isLoginIn;
	}


	public static void setLoginIn(boolean isLoginIn) {
		MainBean.isLoginIn = isLoginIn;
	}
	
	public void logout() throws IOException{
		System.err.println("LOGOUT");
		setLoginIn(false);
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		ec.getFlash().clear();
	    ec.redirect("index.jsf");
	}
	
}
