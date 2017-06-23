package com.ademkoc.services.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class MyJob implements Job
{
	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.out.println("MyJob is executed!");
		
		try {
			URL url = new URL("http://127.0.0.1:8080/gazete-ws/rest/checkNews");
			System.out.println(getUrlSource(url));
			
		} catch (Exception e) {
			e.printStackTrace();
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

        return a.toString();
    }

}