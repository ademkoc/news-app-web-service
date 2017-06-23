package com.ademkoc.models;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "haberler")
public class Haber implements java.io.Serializable {

	private Integer id;
	private Long idFromSource;
	private Kategori kategori;
	private Kaynak kaynak;
	private String haberBaslik, haberMetni, imgUrl;
	private Date tarih;

	public Haber() {
	}

	public Haber(Kategori kategori, Kaynak kaynak, String haberBaslik, String haberMetni, String imgUrl) {
		this.kategori = kategori;
		this.kaynak = kaynak;
		this.haberBaslik = haberBaslik;
		this.haberMetni = haberMetni;
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "kategori_id", nullable = false)
	public Kategori getKategori() {
		return this.kategori;
	}

	public void setKategori(Kategori kategori) {
		this.kategori = kategori;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "kaynak_id", nullable = false)
	public Kaynak getKaynak() {
		return this.kaynak;
	}

	public void setKaynak(Kaynak kaynak) {
		this.kaynak = kaynak;
	}

	@Column(name = "haber_baslik", nullable = false, length = 150)
	public String getHaberBaslik() {
		return this.haberBaslik;
	}

	public void setHaberBaslik(String haberBaslik) {
		this.haberBaslik = haberBaslik;
	}

	@Column(name = "haber_metni", nullable = false)
	public String getHaberMetni() {
		return this.haberMetni;
	}

	public void setHaberMetni(String haberMetni) {
		this.haberMetni = haberMetni;
	}

	@Column(name = "img_url", nullable = false, length = 16777215)
	public String getImgUrl() {
		return this.imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	@Column(name = "tarih")
	public Date getTarih() {
		return tarih;
	}

	public void setTarih(Date tarih) {
		this.tarih = tarih;
	}

	@Column(name = "id_from_source")
	public Long getIdFromSource() {
		return idFromSource;
	}

	public void setIdFromSource(Long idFromSource) {
		this.idFromSource = idFromSource;
	}
	
	

}
