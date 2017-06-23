package com.ademkoc.beans;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import com.ademkoc.dao.KullaniciDAO;
import com.ademkoc.models.Kullanici;

@ManagedBean(name="kullanici")
@RequestScoped
public class UserAuthBean implements Serializable{
	
	private String kullaniciAdi;
	private String sifre;
	
	public String getKullaniciAdi() {
		return kullaniciAdi;
	}
	
	public String getSifre() {
		return sifre;
	}
	
	public void setKullaniciAdi(String kullaniciAdi) {
		this.kullaniciAdi = kullaniciAdi;
	}
	
	public void setSifre(String sifre) {
		this.sifre = sifre;
	}
	
	public String authenticate() {
		try {
			
			KullaniciDAO kullaniciDAO = new KullaniciDAO();
			
			String hql = "FROM Kullanici WHERE eposta = :eposta AND sifre = :sifre";
			
			Map<String, List<String>> map = new HashMap<>();
			map.put("eposta", Arrays.asList(kullaniciAdi));
			map.put("sifre", Arrays.asList(sifre));
			
			List<Kullanici> results = kullaniciDAO.getAllByHQL(hql, map);
			
			if (results.size() == 1) {
		    	MainBean.setLoginIn(true);
		    	return "success";
		    } else {
		    	FacesContext.getCurrentInstance().addMessage(
		    			"formid:pass",
						new FacesMessage("Lütfen bilgilerinizi kontrol ediniz!"));
		        return "fail";
		    }
			
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}		
	}
}
