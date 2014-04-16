package edu.upenn.cis555.finalproject;

import java.io.IOException;
import java.util.List;

import edu.upenn.cis455.storage.CrawlRecord;
import edu.upenn.cis455.storage.DataStore;
import edu.upenn.cis555.url.process.DBLoader;
import edu.upenn.cis555.url.process.StoreUrlToDatabase;

public class DataStoreTest {

	public static void main(String[] args) {
		
		loadUrlIntoTempDB();
		//writeUrlLinks();
		//checkTempDB();
	}
	
	public static void getData() {
		DataStore dataStore = new DataStore("/home/cis455/crawledData/");
		List<String> allUrls = dataStore.getUrlList();
		System.out.println(allUrls.size());
		
		CrawlRecord crawlRecord = dataStore.getWebPage("http://0verlook.tumblr.com/");
		//System.out.println(crawlRecord.getLastCrawled());
		List<String> outLinkList = crawlRecord.getOutLinks();
		for (String link : outLinkList) {
			if(allUrls.contains(link) && !link.equals("http://0verlook.tumblr.com/")) 
				System.out.println("the link is in database: " + link);
			//else System.out.println(link);
		}
		//checkUrl: check whether that url exists
		dataStore.close();
	}
	
	public static void writeUrlLinks() {
		UrlLinkTable ulinkTable = new UrlLinkTable();
		try {
			ulinkTable.getUrlTable();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void loadUrlIntoTempDB() {
		StoreUrlToDatabase s = new StoreUrlToDatabase();
		s.store();
	}
	
	public static void checkTempDB() {
		DBLoader loader = new DBLoader();
		List<String> outlinks = loader.getOutlinksByKey("http://0verlook.tumblr.com/");
		
		DataStore dataStore = new DataStore("/home/cis455/crawledData/");
		CrawlRecord crawlRecord = dataStore.getWebPage("http://0verlook.tumblr.com/");
		List<String> outLinkList = crawlRecord.getOutLinks();
		System.out.println(outlinks.size() == outLinkList.size());
		System.out.println("my database is: " + outlinks);
		System.out.println("actual database is: " + outLinkList);
		System.out.println(loader.checkUrlExists("http://0verlook.tumblr.com/"));
	}

}
