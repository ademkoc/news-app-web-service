package com.ademkoc.beans;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
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
	private Integer id;
	
	@PostConstruct
	public void init() {
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		if(ec.getFlash().containsKey("kullaniciId")) {
			token = (String) ec.getFlash().get("token");
			sifre = (String) ec.getFlash().get("sifre");
			isim = (String) ec.getFlash().get("isim");
			eposta = (String) ec.getFlash().get("eposta");
			id = (Integer) ec.getFlash().get("kullaniciId");
			ec.getFlash().clear();
		}
	}

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
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public List<Kullanici> getKullanicilarList() {
		if (kullanicilarList == null) {
			KullaniciDAO kullaniciDAO = new KullaniciDAO();
			setKullanicilarList(kullaniciDAO.getAll());
		}
		return kullanicilarList;
	}

	public void setKullanicilarList(List<Kullanici> kullanicilarList) {
		this.kullanicilarList = kullanicilarList;
	}
	
	public void saveKullanici() {
	    ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		try {
			
			KullaniciDAO kullaniciDAO = new KullaniciDAO();
			Kullanici kullanici = new Kullanici();
			kullanici.setId(getId());
			kullanici.setEposta(getEposta());
			kullanici.setIsim(getIsim());
			kullanici.setSifre(getSifre());
			kullanici.setToken(getToken());
			
			kullaniciDAO.saveOrUpdate(kullanici);
		
		    ec.getFlash().put(MainBean.FORM_RESULT_KEY, true);
		} catch (Exception e) {
			System.err.println("Kaynak kaydedilemedi. "+e.getMessage());
		    ec.getFlash().put(MainBean.FORM_RESULT_KEY, false);
		}	
		
	    try {
			ec.redirect("main.jsf#kullanicilar-section");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void editKullanici(Kullanici k) {
		this.isim=k.getIsim();
		this.eposta=k.getEposta();
		this.id=k.getId();
		this.token=k.getToken();
		this.sifre=k.getSifre();
		
		try {
			ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
			ec.getFlash().put("kullaniciId", id);
			ec.getFlash().put("isim", isim);
			ec.getFlash().put("eposta", eposta);
			ec.getFlash().put("token", token);
			ec.getFlash().put("sifre", sifre);
			
			ec.redirect("main.jsf#kullanicilar-section");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteKullanici(Kullanici k) {
		try {
			KullaniciDAO kullaniciDAO = new KullaniciDAO();
			kullaniciDAO.delete(k);
			
			FacesContext.getCurrentInstance().getExternalContext().redirect("main.jsf#kullanicilar-section");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
