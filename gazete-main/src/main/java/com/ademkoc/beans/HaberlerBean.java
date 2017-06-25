package com.ademkoc.beans;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import com.ademkoc.dao.HaberDAO;
import com.ademkoc.models.Haber;

@ManagedBean(name="haberler")
@RequestScoped
public class HaberlerBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String haberMetni, resimUrl, baslik;
	private List<Haber> haberList;
	private Integer id, kategoriId, kaynakId;
		
	@PostConstruct
	public void init() {
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		if(ec.getFlash().containsKey("haberId")) {
			resimUrl = (String) ec.getFlash().get("resimUrl");
			haberMetni = (String) ec.getFlash().get("haberMetni");
			baslik = (String) ec.getFlash().get("baslik");
			kategoriId = (Integer) ec.getFlash().get("kategori");
			kaynakId = (Integer) ec.getFlash().get("kaynak");
			id = (Integer) ec.getFlash().get("haberId");
			ec.getFlash().clear();
		}
	}

	public String getHaberMetni() {
		return haberMetni;
	}
	
	public void setHaberMetni(String haberMetni) {
		this.haberMetni = haberMetni;
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
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public void setKategoriId(Integer kategoriId) {
		this.kategoriId = kategoriId;
	}
	
	public Integer getKategoriId() {
		return kategoriId;
	}
	
	public void setKaynakId(Integer kaynakId) {
		this.kaynakId = kaynakId;
	}
	
	public Integer getKaynakId() {
		return kaynakId;
	}
	
	public List<Haber> getHaberList() {
		if (haberList == null) {
			String query = "FROM Haber h JOIN FETCH h.kaynak k JOIN FETCH h.kategori kt"
					+ " WHERE k.id = h.kaynak.id AND kt.id = h.kategori.id";
			
			HaberDAO haberDAO = new HaberDAO();

			List<Haber> result = (List<Haber>) haberDAO.getAllByHQL(query);
			setHaberList(result);
		}
		return haberList;
	}

	public void setHaberList(List<Haber> mHaberList) {
		this.haberList = mHaberList;
	}
	
	public void saveHaber() {
	    ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		try {
			HaberDAO haberDAO = new HaberDAO();
									
			Haber yeniHaber = new Haber();
			yeniHaber.setHaberMetni(haberMetni);
			yeniHaber.setImgUrl(resimUrl);
			yeniHaber.setHaberBaslik(baslik);
			
			haberDAO.save(yeniHaber);
		
		    ec.getFlash().put(MainBean.FORM_RESULT_KEY, true);
		} catch (Exception e) {
			System.err.println("Kaynak kaydedilemedi. "+e.getMessage());
		    ec.getFlash().put(MainBean.FORM_RESULT_KEY, false);
		}
		
	    try {
			ec.redirect("main.jsf#haberler-section");
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}

	public void editHaber(Haber haber) {
		this.baslik=haber.getHaberBaslik();
		this.haberMetni=haber.getHaberMetni();
		this.id=haber.getId();
		this.resimUrl=haber.getImgUrl();
		this.kaynakId=haber.getKaynak().getId();
		this.kategoriId=haber.getKategori().getId();
		
		try {
			ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
			ec.getFlash().put("haberId", id);
			ec.getFlash().put("baslik", baslik);
			ec.getFlash().put("haberMetni", haberMetni);
			ec.getFlash().put("resimUrl", resimUrl);
			ec.getFlash().put("kategori", kategoriId);
			ec.getFlash().put("kaynak", kaynakId);
			
			ec.redirect("main.jsf#haberler-section");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteHaber(Haber h) {
		try {
			HaberDAO haberDAO = new HaberDAO();
			haberDAO.delete(h);
			
			FacesContext.getCurrentInstance().getExternalContext().redirect("main.jsf#haberler-section");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
