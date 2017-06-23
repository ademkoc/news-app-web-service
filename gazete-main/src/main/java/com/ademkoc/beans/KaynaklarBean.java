package com.ademkoc.beans;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import com.ademkoc.dao.KaynakDAO;
import com.ademkoc.models.Kaynak;

@ManagedBean(name="kaynaklar")
@RequestScoped
public class KaynaklarBean implements Serializable{

	private String kaynakAdi, imgUrl;
	private List<Kaynak> kaynaklarList;
	
	public String getKaynakAdi() {
		return kaynakAdi;
	}
	
	public void setKaynakAdi(String kaynakAdi) {
		this.kaynakAdi = kaynakAdi;
	}
	
	public void redictPage(boolean isSuccess) throws IOException {
	    ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
	    ec.getFlash().put(MainBean.KEY, isSuccess);
	    ec.redirect("main.jsf#kaynaklar-section");
	}
	
	public void saveKaynak() throws IOException {
				
		try {
			KaynakDAO kaynakDAO = new KaynakDAO();
			
			Kaynak yeniKaynak = new Kaynak(kaynakAdi, imgUrl);
			
			kaynakDAO.save(yeniKaynak);

			redictPage(true);
		} catch (Exception e) {
			System.err.println("Kaynak kaydedilemedi. "+e.getMessage());
			redictPage(false);
		}
			
	}
	
	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public List<Kaynak> getKaynaklarList() {
		
		KaynakDAO kaynakDAO = new KaynakDAO();
		
		setKaynaklarList(kaynakDAO.getAll());
		
		return kaynaklarList;
	}

	public void setKaynaklarList(List<Kaynak> kaynaklarList) {
		this.kaynaklarList = kaynaklarList;
	}

}
