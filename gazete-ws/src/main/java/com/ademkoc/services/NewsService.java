package com.ademkoc.services;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.ademkoc.dao.HaberDAO;
import com.ademkoc.dao.KategoriDAO;
import com.ademkoc.dao.KaynakDAO;
import com.ademkoc.dao.KullaniciDAO;
import com.ademkoc.models.Haber;
import com.ademkoc.models.Kategori;
import com.ademkoc.models.Kaynak;
import com.ademkoc.models.Kullanici;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Path("/")
public class NewsService {

	@GET
	@Path("getNewsUrls")
	@Produces(MediaType.APPLICATION_JSON + "; charset=utf-8")
	public Map<String, List<String>> getNewsUrls() {
		String source = "null";
		List<String> urlList = new ArrayList<>();
		String patternStr = "<div class=\"sliderWrapper\">(.*?)<div class=\"headlineNumeric type01 view10 boxShadowSet\">(.*?)</div>";
		try {
			URL url = new URL("http://www.sabah.com.tr");
			source = getUrlSource(url);
			
			Pattern pattern = Pattern.compile(patternStr);
	        Matcher matcher = pattern.matcher(source);

	        if (matcher.find()) {
	        	for (int i = 0; i < matcher.groupCount(); i++) {					
					patternStr = "<a href=\"(.*?)\" title=\"(.*?)\" target=\"_blank\">(.*?)<img src=\"(.*?)\" data-id=\"(.*?)\" alt=\"(.*?)\" width=\"722\" height=\"381\" />(.*?)</a>";
					
					pattern = Pattern.compile(patternStr);
					Matcher matcher2 = pattern.matcher(matcher.group(i));
		        	while (matcher2.find()) {
		        		String groupStr = matcher2.group(1);
		        		if (!groupStr.contains("http")) {
		        			groupStr = "http://www.sabah.com.tr" + groupStr;
						}
		        		urlList.add(groupStr);
		        	}
				}	        	
	        } else {
	        	System.err.println("Not found");
	        }
	        
			Map<String, List<String>> hashMap = new HashMap<String, List<String>>();
			hashMap.put("linkler", urlList);
			
			return hashMap;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@GET
	@Path("checkNews")
	@Produces(MediaType.TEXT_HTML + "; charset=utf-8")
	public String checkNews(@QueryParam("sendNotification") boolean b) {
		Map<String, List<String>> urlsMap = getNewsUrls();
		List<String> urlsList = urlsMap.get("linkler");	
		
		for (int i = 0; i < urlsList.size(); i++) {
			try {
				Haber h = getNews(new URL(urlsList.get(i)));
				
				 // sendNotification
				 
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "CEMAL OKKA";
	}
	
	@GET
	@Path("getNews")
	@Produces(MediaType.APPLICATION_JSON+"; charset=utf-8")
	public Haber getNews(@QueryParam("url") String url) {
		
		try {
			Haber haber = getNews(new URL(url));
			return haber;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public void pushFCMNotification(Haber h) throws Exception{
	
	   String authKey = "AAAAMina2VU:APA91bFYQK6JDyRZCmLYSYJxp8zVNQ9wHxAsKPcb3DddIrT7b7fqLCtPvWWwxV-hy5C13Qunio-Q5twWvDMdxbwU7sw6KRvUvwqDEwvuCk4LkOB3Nh8d9RneZhvvZVXG2g-plrWNbm0u"; 
	   String FMCurl = "https://fcm.googleapis.com/fcm/send"; 
	
	   URL url = new URL(FMCurl);
	   HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	
	   conn.setUseCaches(false);
	   conn.setDoInput(true);
	   conn.setDoOutput(true);
	
	   conn.setRequestMethod("POST");
	   conn.setRequestProperty("Authorization","key="+authKey);
	   conn.setRequestProperty("Content-Type","application/json");
	
	   conn.connect();
	
	   OutputStream os = conn.getOutputStream();
	   OutputStreamWriter outputStreamWriter = new OutputStreamWriter(os, "UTF-8");
	   BufferedWriter bw = new BufferedWriter(outputStreamWriter);
	   PrintWriter printWriter = new PrintWriter(bw);
	
	
	   KullaniciDAO kullaniciDAO = new KullaniciDAO();
	   List<Kullanici> k = kullaniciDAO.getAllByHQL("SELECT new Kullanici(k.token) FROM Kullanici k");
	
	   for (Kullanici kullanici : k) {
		   
		   if (kullanici.getToken().equalsIgnoreCase("admin"))
			   return;
		   
		   ObjectMapper mapper = new ObjectMapper();
		   mapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
		    
		   ObjectNode node2 = mapper.getNodeFactory().objectNode();
		   node2.put("body", h.getHaberBaslik());
		   node2.put("priority", "high");
			
		   ObjectNode node = mapper.getNodeFactory().objectNode();
		   node.put("to", kullanici.getToken());
		   node.put("data", node2);
		   
		   printWriter.print(mapper.writeValueAsString(node));
		   printWriter.flush();
		   
		   InputStream  is = conn.getInputStream();
		   InputStreamReader isr = new InputStreamReader(is, "UTF-8");
		   BufferedReader br = new BufferedReader(isr);
	
		   final StringBuilder sb = new StringBuilder();
			
		   
		   for (String line; (line = br.readLine()) != null;) {
			   sb.append(line);
		   }
	
		   final String responseText = sb.toString();
		   System.out.println("response:\n" + responseText);
	   }
	}

	private String getUrlSource(URL url) throws IOException {
        URLConnection urlConnection = url.openConnection();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
        		urlConnection.getInputStream(), "UTF-8"));
        String inputLine;
        StringBuilder a = new StringBuilder();
        while ((inputLine = bufferedReader.readLine()) != null)
            a.append(inputLine);
        bufferedReader.close();
        
        return a.toString().replaceAll("\n\r", "").replaceAll("\t", "").replaceAll("\\s{2,}", "");
    }
	
	private Haber getNews(URL url) {
		if (url.getFile().contains("galeri")) {
			return null;
		}
		try {
			
			String source = getUrlSource(url);
			System.out.println(source);
			
			if (getKategori(source) == null) {
				System.out.println("Kategori is null");
				return null;
			}
			
	        HaberDAO haberDAO = new HaberDAO();
	        
			Haber yeniHaber = new Haber();
			yeniHaber.setImgUrl(getHaberResmi(source));
			yeniHaber.setHaberBaslik(getHaberBaslik(source));
			yeniHaber.setHaberMetni(getHaberMetni(source));
			yeniHaber.setKaynak(getKaynak(url));
			yeniHaber.setIdFromSource(getId(yeniHaber.getImgUrl()));
			yeniHaber.setKategori(getKategori(source));
			
	        Calendar cal = Calendar.getInstance();
	        cal.setTime(new java.util.Date());
	        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH)-1);
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        
	        String hql = "FROM Haber h "
	        		+ "WHERE h.idFromSource="+yeniHaber.getIdFromSource()+" AND h.tarih > '"+sdf.format(cal.getTime()).toString()+"'";
	        System.out.println("HQL: "+hql);
	        
	        List<Haber> listHaber = haberDAO.getAllByHQL(hql);
	        if (listHaber.size() < 1) {
		        haberDAO.save(yeniHaber);	//Haber yok. Direk ekle
	        } else {
	        	//Haber var.
				for (Haber haber : listHaber) {
					//Güncelleme gerekiyor mu?
					if (!haber.getHaberBaslik().trim().contentEquals(yeniHaber.getHaberBaslik().trim()) 
							| !haber.getHaberMetni().trim().contentEquals(yeniHaber.getHaberMetni().trim())) {
						yeniHaber.setId(haber.getId());
						haberDAO.saveOrUpdate(yeniHaber);
					}
				}
			}
	        
	        return yeniHaber;
	        
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
		
	private String getHaberResmi(String source) {
        /*Haber Resmi*/
        String haberResimRegex = "<div class=\"col-sm-12 text-center view20\">(.*?)</div>";
        String haberUrl = "";
        
        Pattern pattern = Pattern.compile(haberResimRegex);
        Matcher matcher = pattern.matcher(source);
        
        if (matcher.find()) {
        	String regex = "";
            
        	if (matcher.group(1).contains("data-original")) {
				regex = "<figure class=\"newsImage\">(.*?)<img src=\"(.*?)\" data-original=\"(.*?)\" alt=\"(.*?)\" />";
			} else {
				regex = "<figure class=\"newsImage\">(.*?)<img src=\"(.*?)\" alt=\"(.*?)\" />";
			}
            
            pattern = Pattern.compile(regex, Pattern.DOTALL);
			matcher = pattern.matcher(matcher.group(0));
			
			if (matcher.find()) {
				if (matcher.groupCount() > 3) 
					haberUrl = matcher.group(3);
				else
					haberUrl = matcher.group(2);
			} else {
				haberUrl = "https://www.euractiv.com/wp-content/uploads/sites/2/2014/03/news-default.jpeg";
			}
        }
		return haberUrl;
	}
	
	private String getHaberBaslik(String source) {
		/*Haber Başlık*/
		String haberBaslikRegex = "<h1 class=\"pageTitle\">(.*?)</h1>";
		
		Pattern pattern = Pattern.compile(haberBaslikRegex);
        Matcher matcher = pattern.matcher(source);
        
        if(matcher.find()) {
        	return matcher.group(1).replaceAll("\\<[^>]*>","");
        }
        return null;
	}
	
	private String getHaberMetni(String source) {
        /*Haber Metni*/
        String haberMetni = "";
        String spotText = "";
        
        String haberSpotRegex = "<div class=\"col-sm-12 view20\"> <h2 class=\"spot\">(.*?)</h2><div class=\"newsDetailText\"><div class=\"newsBox\">(.*?)</div></div></div>";
        
		Pattern pattern = Pattern.compile(haberSpotRegex);
        Matcher matcher = pattern.matcher(source);
        
        if (matcher.find()) {
			spotText = matcher.group(1).replaceAll("\\<[^>]*>","");
			haberMetni = spotText + "\n";
        } else {
			System.out.println("Haber Spot Metni BULUNAMADI");
		}
        
        String haberMetniRegex = "<p>(.*?)</p>";
        pattern = Pattern.compile(haberMetniRegex);
        matcher = pattern.matcher(matcher.group(2));
        
        while (matcher.find()) {
        	for (int i = 1; i <= matcher.groupCount(); i++) {
        		String paragh = matcher.group(i);
        		if (paragh.contains("videoGallery")) {
					continue;
				}
                haberMetni += paragh.replaceAll("\\<[^>]*>","")+"\n\n";
            }
        }
                
        return haberMetni;
	}
	
	private Kategori getKategori(String source) {
		/*Haber Kategori*/
		
		String haberKategoriRegex = "<ul class=\"breadcrumb\"><li><a href=\"(.*?)\">(.*?)</a></li><li><a href=\"(.*?)\">(.*?) Haberleri</a></li><li>(.*?)</li></ul>";
		
		Pattern pattern = Pattern.compile(haberKategoriRegex);
        Matcher matcher = pattern.matcher(source);
        
        if(matcher.find()) {
    		String haberKategori = matcher.group(4);

    		KategoriDAO kategoriDAO = new KategoriDAO();
    		List<Kategori> list = kategoriDAO.getAll();
    		
    		for (Kategori kategori : list) {
				if (kategori.getKategoriAdi().equalsIgnoreCase(haberKategori)) {
					return kategori;
				}
			}
    		
			Kategori k = new Kategori(haberKategori);
			kategoriDAO.save(k);
			return k;
		}
        return null;
	}

	private Kaynak getKaynak(URL url) {
        /*Kaynak Alma*/
        String kaynakAdi = url.getHost().substring(4);
        kaynakAdi = kaynakAdi.substring(0, 1).toUpperCase()+kaynakAdi.substring(1);

        KaynakDAO kaynakDAO = new KaynakDAO();
        List<Kaynak> list = kaynakDAO.getAll();
        for (Kaynak kaynak : list) {
			if (kaynak.getKaynakAdi().equalsIgnoreCase(kaynakAdi)) {
				return kaynak;
			}
		}
        
		Kaynak k = new Kaynak();
		k.setKaynakAdi(kaynakAdi);
		k.setImgUrl("http://i.sabah.com.tr/sbh/2016/09/09/1473431314550.png");
    	kaynakDAO.save(k);
    	return k;
	}
	
	private Long getId(String imgUrl) {
        /*Kaynaktan Haber Id*/
        int lenght = imgUrl.length();
        
        if (lenght > 16) {
	        int indis = imgUrl.indexOf(".jpg");
	        
	        if (indis < 0) {
	        	indis = imgUrl.indexOf(".jpeg");
			}

	        String id = imgUrl.substring(indis-13, indis);
	        
	        return Long.parseLong(id);
	        
		} else {
	        return null;
		}
	}

}