package com.ademkoc.models;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "kullanicilar", uniqueConstraints = @UniqueConstraint(columnNames = "eposta"))
public class Kullanici implements java.io.Serializable {

	private Integer id;
	private String isim, eposta, sifre, token;

	public Kullanici() {
	}
	
	public Kullanici(String token) {
		this.token = token;
	}
	
	public Kullanici(int id, String eposta) {
		this.id=id;
		this.eposta=eposta;
	}

	public Kullanici(String isim, String eposta, String sifre) {
		this.isim = isim;
		this.eposta = eposta;
		this.sifre = sifre;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "isim", length = 100)
	public String getIsim() {
		return this.isim;
	}

	public void setIsim(String isim) {
		this.isim = isim;
	}

	@Column(name = "eposta", unique = true, nullable = false, length = 50)
	public String getEposta() {
		return this.eposta;
	}

	public void setEposta(String eposta) {
		this.eposta = eposta;
	}

	@Column(name = "sifre", length = 15)
	public String getSifre() {
		return this.sifre;
	}

	public void setSifre(String sifre) {
		this.sifre = sifre;
	}

	public String getToken() {
		return token;
	}
	
	@Column(name = "token", nullable = false, length = 152)
	public void setToken(String token) {
		this.token = token;
	}
}
