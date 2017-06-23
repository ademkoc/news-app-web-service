package com.ademkoc.beans;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import com.ademkoc.dao.KategoriDAO;
import com.ademkoc.models.Kategori;

@ManagedBean(name="kategoriler")
@RequestScoped
public class KategorilerBean implements Serializable{
	
	private String kategori;
	private List<Kategori> kategoriList;
	
	public String getKategori() {
		return kategori;
	}
	
	public void setKategori(String kategori) {
		this.kategori = kategori;
	}
	
	public void saveKategori() {
		try {
			KategoriDAO kategoriDAO = new KategoriDAO();
			
			Kategori yeniKategori = new Kategori(kategori);
			kategoriDAO.save(yeniKategori);

			redictPage(true);
		} catch (Exception e) {
			System.err.println("Kaynak kaydedilemedi. "+e.getMessage());
			redictPage(false);
		}
	}

	public void redictPage(boolean isSuccess) {
		try {
			ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
			ec.getFlash().put(MainBean.KEY, isSuccess);
			ec.redirect("main.jsf#kategoriler-section");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<Kategori> getKategoriList() {
		
		KategoriDAO kategoriDAO = new KategoriDAO();
		
		setKategoriList(kategoriDAO.getAll());
		
		return kategoriList;
	}

	public void setKategoriList(List<Kategori> kategoriList) {
		this.kategoriList = kategoriList;
	}
}
