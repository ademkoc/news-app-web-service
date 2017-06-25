package com.ademkoc.beans;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import com.ademkoc.dao.KaynakDAO;
import com.ademkoc.models.Kaynak;

@ManagedBean(name="kaynaklar")
@RequestScoped
public class KaynaklarBean implements Serializable{

	private static final long serialVersionUID = 1L;
	private Integer id;
	private String kaynakAdi, imgUrl;
	private List<Kaynak> kaynaklarList;
	
	@PostConstruct
	public void init() {
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		if(ec.getFlash().containsKey("kaynakId")) {
			kaynakAdi = (String) ec.getFlash().get("kaynakAdi");
			imgUrl = (String) ec.getFlash().get("imgUrl");
			id = (Integer) ec.getFlash().get("kaynakId");
			ec.getFlash().clear();
		}
	}
	
	public String getKaynakAdi() {
		return kaynakAdi;
	}
	
	public void setKaynakAdi(String kaynakAdi) {
		this.kaynakAdi = kaynakAdi;
	}
	
	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public List<Kaynak> getKaynaklarList() {
		if (kaynaklarList == null) {
			KaynakDAO kaynakDAO = new KaynakDAO();
			setKaynaklarList(kaynakDAO.getAll());
		}
		return kaynaklarList;
	}

	public void setKaynaklarList(List<Kaynak> kaynaklarList) {
		this.kaynaklarList = kaynaklarList;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public void saveKaynak() {
	    ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		try {
			KaynakDAO kaynakDAO = new KaynakDAO();
			
			Kaynak yeniKaynak = new Kaynak();
			yeniKaynak.setId(getId());
			yeniKaynak.setImgUrl(getImgUrl());
			yeniKaynak.setKaynakAdi(getKaynakAdi());
			
			kaynakDAO.saveOrUpdate(yeniKaynak);
	
		    ec.getFlash().put(MainBean.FORM_RESULT_KEY, true);
		} catch (Exception e) {
			System.err.println("Kaynak kaydedilemedi. "+e.getMessage());
		    ec.getFlash().put(MainBean.FORM_RESULT_KEY, false);
		}
		
	    try {
			ec.redirect("main.jsf#kaynaklar-section");
		} catch (IOException e) {
			e.printStackTrace();
		}
			
	}

	public void editKaynak(Kaynak kaynak) {
		this.id=kaynak.getId();
		this.kaynakAdi=kaynak.getKaynakAdi();
		this.imgUrl=kaynak.getImgUrl();
		
		try {
			ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
			ec.getFlash().put("kaynakId", id);
			ec.getFlash().put("kaynakAdi", kaynakAdi);
			ec.getFlash().put("imgUrl", imgUrl);
			ec.redirect("main.jsf#kaynaklar-section");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteKaynak(Kaynak k) {
		try {
			KaynakDAO kaynakDAO = new KaynakDAO();
			kaynakDAO.delete(k);
			
			FacesContext.getCurrentInstance().getExternalContext().redirect("main.jsf#kaynaklar-section");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}