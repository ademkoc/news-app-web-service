package com.ademkoc.beans;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import com.ademkoc.dao.KategoriDAO;
import com.ademkoc.models.Kategori;

@ManagedBean(name="kategoriler")
@RequestScoped
public class KategorilerBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String kategori;
	private Integer id;
	private List<Kategori> kategoriList;
	
	@PostConstruct
	public void init() {
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		if(ec.getFlash().containsKey("kategoriId")) {
			kategori = (String) ec.getFlash().get("kategoriAdi");
			id = (Integer) ec.getFlash().get("kategoriId");
			ec.getFlash().clear();
		}
	}
	
	public String getKategori() {
		return kategori;
	}
	
	public void setKategori(String kategori) {
		this.kategori = kategori;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public void saveKategori() {
		
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		
		try {
			KategoriDAO kategoriDAO = new KategoriDAO();
			
			Kategori yeniKategori = new Kategori();
			yeniKategori.setId(getId());
			yeniKategori.setKategoriAdi(getKategori());
			kategoriDAO.saveOrUpdate(yeniKategori);

			ec.getFlash().put(MainBean.FORM_RESULT_KEY, true);

		} catch (Exception e) {
			System.err.println("Kaynak kaydedilemedi. "+e.getMessage());
			ec.getFlash().put(MainBean.FORM_RESULT_KEY, false);
		}
		
		try {
			ec.redirect("main.jsf#kategoriler-section");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<Kategori> getKategoriList() {
		if (kategoriList == null) {
			KategoriDAO kategoriDAO = new KategoriDAO();
			setKategoriList(kategoriDAO.getAll());
		}
		return kategoriList;
	}

	public void setKategoriList(List<Kategori> kategoriList) {
		this.kategoriList = kategoriList;
	}
	
	public void editKategori(Kategori k) {
		this.kategori=k.getKategoriAdi();
		this.id=k.getId();
		
		try {
			ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
			ec.getFlash().put("kategoriId", id);
			ec.getFlash().put("kategoriAdi", kategori);
			ec.redirect("main.jsf#kategoriler-section");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteKategori(Kategori k) {
		try {
			KategoriDAO kategoriDAO = new KategoriDAO();
			kategoriDAO.delete(k);
			
			FacesContext.getCurrentInstance().getExternalContext().redirect("main.jsf#kategoriler-section");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
