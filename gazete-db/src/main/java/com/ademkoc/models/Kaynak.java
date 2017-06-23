package com.ademkoc.models;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "kaynaklar")
public class Kaynak implements java.io.Serializable {

	private Integer id;
	private String kaynakAdi;
	private String imgUrl;

	public Kaynak() {
	}

	public Kaynak(String kaynakAdi, String imgUrl) {
		this.kaynakAdi = kaynakAdi;
		this.imgUrl = imgUrl;
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

	@Column(name = "kaynak_adi", nullable = false, length = 50)
	public String getKaynakAdi() {
		return this.kaynakAdi;
	}

	public void setKaynakAdi(String kaynakAdi) {
		this.kaynakAdi = kaynakAdi;
	}
	
	@Column(name = "img_url", nullable = false, length = 16777215)
	public String getImgUrl() {
		return this.imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
}
