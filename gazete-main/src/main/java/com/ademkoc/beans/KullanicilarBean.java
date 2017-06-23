package com.ademkoc.beans;

import java.io.IOException;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import com.ademkoc.dao.KullaniciDAO;
import com.ademkoc.models.Kullanici;

@ManagedBean(name="kullanicilar")
@RequestScoped
public class KullanicilarBean {

	private String token, sifre, isim, eposta;
	private List<Kullanici> kullanicilarList;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getSifre() {
		return sifre;
	}

	public void setSifre(String sifre) {
		this.sifre = sifre;
	}

	public String getIsim() {
		return isim;
	}

	public void setIsim(String isim) {
		this.isim = isim;
	}

	public String getEposta() {
		return eposta;
	}

	public void setEposta(String eposta) {
		this.eposta = eposta;
	}
	
	public void saveKullanici() throws IOException {
		try {
			
			KullaniciDAO kullaniciDAO = new KullaniciDAO();
			Kullanici kullanici = new Kullanici();
			kullanici.setEposta(getEposta());
			kullanici.setIsim(getIsim());
			kullanici.setSifre(getSifre());
			kullanici.setToken(getToken());
			
			kullaniciDAO.save(kullanici);
		
			redictPage(true);
		} catch (Exception e) {
			System.err.println("Kaynak kaydedilemedi. "+e.getMessage());
			redictPage(false);
		}	
	}
	
	public void redictPage(boolean isSuccess) throws IOException {
	    ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
	    ec.getFlash().put(MainBean.KEY, isSuccess);
	    ec.redirect("main.jsf#haberler-section");
	}

	public List<Kullanici> getKullanicilarList() {
		KullaniciDAO kullaniciDAO = new KullaniciDAO();
		
		setKullanicilarList(kullaniciDAO.getAll());
		
		return kullanicilarList;
	}

	public void setKullanicilarList(List<Kullanici> kullanicilarList) {
		this.kullanicilarList = kullanicilarList;
	}
	
}
