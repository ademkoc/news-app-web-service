package com.ademkoc.models;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "kategoriler", uniqueConstraints = @UniqueConstraint(columnNames = "kategori_adi"))
public class Kategori implements java.io.Serializable {

	private Integer id;
	private String kategoriAdi;

	public Kategori() {
	}

	public Kategori(String kategoriAdi) {
		this.kategoriAdi = kategoriAdi;
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

	@Column(name = "kategori_adi", unique = true, nullable = false, length = 50)
	public String getKategoriAdi() {
		return this.kategoriAdi;
	}

	public void setKategoriAdi(String kategoriAdi) {
		this.kategoriAdi = kategoriAdi;
	}
}
