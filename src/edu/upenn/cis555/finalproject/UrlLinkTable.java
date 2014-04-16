package edu.upenn.cis555.finalproject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.upenn.cis455.storage.CrawlRecord;
import edu.upenn.cis455.storage.DataStore;

public class UrlLinkTable {
	List<String> allUrls;
	DataStore dataStore;

	public void getUrlTable() throws IOException {
		dataStore = new DataStore("/home/cis455/export/hw2env/");
		//get all the urls of the data storage
		allUrls = dataStore.getUrlList();
		int length = allUrls.size();
		int fileNum = length/1000 + 1;
		createFiles(fileNum);
		
		for(int n = 0; n < fileNum; n++) {
			for(int i = n*1000; i < Math.min((n+1)*1000, length); i++) {
				File file = new File("/home/cis455/HadoopTest/url_test/" + "url" + n + ".txt");
				List<String> validOutLinks = getValidUrlList(allUrls.get(i));
				if(validOutLinks.size() > 0) {
					System.out.println("valid outlinks size is larger than 0");
					writeToFile(allUrls.get(i), validOutLinks, file);
				}
				System.out.println("outlinks size is 0");
			}
		}
		
		dataStore.close();
	}
	
	public void createFiles(int num) throws IOException {
		String dir = "/home/cis455/HadoopTest/url_test/";
		for(int i = 0; i < num; i++) {
			File file = new File(dir, "url" + i + ".txt");
			if(file.exists()) file.createNewFile();
		}
	}
	
	public boolean urlIsInDatabase(String url) {
		if(allUrls == null || allUrls.size() == 0) return false;
System.out.println(dataStore.checkUrl(url) + "... " + url);
		if(dataStore.checkUrl(url)) return true;
		//if(allUrls.contains(url)) return true;
		return false;		
	}
	
	public List<String> getValidUrlList(String url) {		
		CrawlRecord crawlRecord = dataStore.getWebPage(url);
		List<String> outLinkList = crawlRecord.getOutLinks();
		List<String> result = new ArrayList<String>();;
		if(outLinkList.size() > 0) {			
			for(String outLinkUrl : outLinkList) {
				if(urlIsInDatabase(outLinkUrl) && !outLinkUrl.equals(url)) {
					result.add(outLinkUrl);
				} 
			}
		} 
		return result;
	}
	
	public void writeToFile(String url, List<String> outLinks, File file) throws IOException {
		FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
		BufferedWriter bw = new BufferedWriter(fw);
		StringBuffer sb = new StringBuffer();
		sb.append(url + "\t" + "1.0|");
		sb.append(outLinks.get(0));
		for(int i = 1; i < outLinks.size(); i++) {
			sb.append(" " + outLinks.get(i));
		}
		sb.append("\n");
		bw.write(sb.toString());
		bw.close();

		System.out.println("Done");
	}
}
