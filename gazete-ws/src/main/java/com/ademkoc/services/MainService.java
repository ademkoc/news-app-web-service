package com.ademkoc.services;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.ademkoc.dao.HaberDAO;
import com.ademkoc.dao.KategoriDAO;
import com.ademkoc.dao.KullaniciDAO;
import com.ademkoc.models.Haber;
import com.ademkoc.models.Kategori;
import com.ademkoc.models.Kullanici;

@Path("/main")
public class MainService {

	@GET
	@Path("/getKategoriler")
	@Produces(MediaType.APPLICATION_JSON + "; charset=utf-8")
	public List<Kategori> getKategoriler() {
		
		KategoriDAO kategoriDAO = new KategoriDAO();
		
		List<Kategori> result = (List<Kategori>) kategoriDAO.getAll();

		return result;
	}

	@GET
	@Path("/getHaberler")
	@Produces(MediaType.APPLICATION_JSON + "; charset=utf-8")
	public List<Haber> getHaberler(@QueryParam("id") List<Integer> ids) {
	
		String query = "FROM Haber h JOIN FETCH h.kaynak k JOIN FETCH h.kategori kt"
				+ " WHERE k.id = h.kaynak.id AND kt.id = h.kategori.id AND h.kategori.id IN ("
				+ ids.toString().substring(1, ids.toString().length()-1)+") ORDER BY h.tarih DESC";
		
		
		HaberDAO haberDAO = new HaberDAO();

		List<Haber> result = (List<Haber>) haberDAO.getAllByHQL(query);
	    
		return result;
	}
	
	@POST
	@Path("/sendUser")
	@Produces(MediaType.APPLICATION_JSON + "; charset=utf-8")
	public Response saveUser(Kullanici kullanici) {
		
		try {
			
			KullaniciDAO kullaniciDAO = new KullaniciDAO();
			
			String hql = "SELECT new Kullanici(k.id, k.eposta) FROM Kullanici k WHERE eposta='"+kullanici.getEposta()+"'";
			
			List<Kullanici> list = kullaniciDAO.getAllByHQL(hql);

			for (Kullanici k : list) {
				if(k.getEposta().equalsIgnoreCase(kullanici.getEposta())){
					kullanici.setId(k.getId());
				}
			}
			kullaniciDAO.saveOrUpdate(kullanici);
						
			return Response.status(Status.OK).build();
		} catch (Exception e) { 
			e.printStackTrace();
		}
		
		return Response.status(Status.INTERNAL_SERVER_ERROR).build();
	}

}
