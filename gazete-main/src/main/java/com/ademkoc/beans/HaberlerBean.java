package com.ademkoc.beans;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import com.ademkoc.dao.HaberDAO;
import com.ademkoc.dao.KategoriDAO;
import com.ademkoc.dao.KaynakDAO;
import com.ademkoc.models.Haber;
import com.ademkoc.models.Kategori;
import com.ademkoc.models.Kaynak;

@ManagedBean(name="haberler")
@RequestScoped
public class HaberlerBean implements Serializable{
	
	private List<SelectItem> kategoriler = null, kaynaklar = null;
	private String selectedKaynak, selectedKategori, haberMetni, resimUrl, baslik;
	private List<Haber> haberList;
	
	public HaberlerBean() {

	}

	public List<SelectItem> getKategoriler() {
		if (kategoriler == null) 
			setKategoriler(new ArrayList<SelectItem>());
		else
			return kategoriler;
		
		KategoriDAO kategoriDAO = new KategoriDAO();
		
		List<Kategori> categoryList = kategoriDAO.getAll();

		for (Kategori kategori : categoryList) {
			kategoriler.add(new SelectItem(kategori.getId().toString(), kategori.getKategoriAdi().toString()));
		}
		
		return kategoriler;
	}
	
	public void setKategoriler(List<SelectItem> options) {
		this.kategoriler = options;
	}

	public List<SelectItem> getKaynaklar() {
		if (kaynaklar == null) {
			setKaynaklar(new ArrayList<SelectItem>());
		}
		
		KaynakDAO kaynakDAO = new KaynakDAO();
		
		List<Kaynak> kaynaklarList = kaynakDAO.getAll();

		for (Kaynak kaynak : kaynaklarList) {
			kaynaklar.add(new SelectItem(kaynak.getId().toString(), kaynak.getKaynakAdi().toString()));
		}
		
		return kaynaklar;
	}

	public void setKaynaklar(List<SelectItem> kaynaklar) {
		this.kaynaklar = kaynaklar;
	}
	
	public void saveHaber() throws IOException {
		
		try {
			KategoriDAO kategoriDAO = new KategoriDAO();			
			KaynakDAO kaynakDAO = new KaynakDAO();
			HaberDAO haberDAO = new HaberDAO();
			
			Kategori kategori=(Kategori) kategoriDAO.getById(Integer.valueOf(getSelectedKategori()));
			Kaynak kaynak=(Kaynak) kaynakDAO.getById(Integer.valueOf(getSelectedKaynak()));
						
			Haber yeniHaber = new Haber();
			yeniHaber.setHaberMetni(haberMetni);
			yeniHaber.setKategori(kategori);
			yeniHaber.setKaynak(kaynak);
			yeniHaber.setImgUrl(resimUrl);
			yeniHaber.setHaberBaslik(baslik);
			
			haberDAO.save(yeniHaber);
		
			redictPage(true);
		} catch (Exception e) {
			System.err.println("Kaynak kaydedilemedi. "+e.getMessage());
			redictPage(false);
		}
	
	}
	
	public String getHaberMetni() {
		return haberMetni;
	}
	
	public void setHaberMetni(String haberMetni) {
		this.haberMetni = haberMetni;
	}

	public String getSelectedKategori() {
		return selectedKategori;
	}

	public void setSelectedKategori(String selectedKategori) {
		this.selectedKategori = selectedKategori;
	}

	public String getSelectedKaynak() {
		return selectedKaynak;
	}

	public void setSelectedKaynak(String selectedKaynak) {
		this.selectedKaynak = selectedKaynak;
	}
	
	public void redictPage(boolean isSuccess) throws IOException {
	    ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
	    ec.getFlash().put(MainBean.KEY, isSuccess);
	    ec.redirect("main.jsf#haberler-section");
	}

	public String getResimUrl() {
		return resimUrl;
	}

	public void setResimUrl(String resimUrl) {
		this.resimUrl = resimUrl;
	}

	public String getBaslik() {
		return baslik;
	}

	public void setBaslik(String baslik) {
		this.baslik = baslik;
	}

	public List<Haber> getHaberList() {
		
		String query = "FROM Haber h JOIN FETCH h.kaynak k JOIN FETCH h.kategori kt"
				+ " WHERE k.id = h.kaynak.id AND kt.id = h.kategori.id ORDER BY h.tarih DESC";
		
		HaberDAO haberDAO = new HaberDAO();

		List<Haber> result = (List<Haber>) haberDAO.getAllByHQL(query);
		setHaberList(result);
		
		return haberList;
	}

	public void setHaberList(List<Haber> mHaberList) {
		this.haberList = mHaberList;
	}

}
