package edu.upenn.cis555.url.process;

import java.util.ArrayList;
import java.util.List;

import edu.upenn.cis455.storage.CrawlRecord;
import edu.upenn.cis455.storage.DataStore;

public class StoreUrlToDatabase {
	public DataStore dataStore;
	public DBLoader dbLoader;
	public boolean Finished;
	
	public void store() {
//		dataStore = new DataStore("/home/cis455/crawledData/");
//		dbLoader = new DBLoader();
//		for(String keyUrl : dataStore.getUrlList()) {
//			System.out.println("keyUrl is ... " + keyUrl);
//			CrawlRecord crawlRecord = dataStore.getWebPage(keyUrl);
//			
//			List<String> outlinks = removeDupsAndSelf(crawlRecord.getOutLinks(), keyUrl);
//			
//			System.out.println("and the outlinks are ... " + outlinks);
//			dbLoader.loadUrlDb(keyUrl, outlinks);
//			
//		}
		
		dbLoader = new DBLoader();
		dbLoader.deleteFiles();
		loadSampleData();
		updateDB();
	}
	
	//when loading the crawledData into my database, do some processing on the outlinks
	public void loadSampleData() {
		List<String> l1 = new ArrayList<String>();
		l1.add("u2");l1.add("u3");l1.add("u3");l1.add("u4");l1.add("u5");
		l1 = removeDupsAndSelf(l1, "u1");
		dbLoader.loadUrlDb("u1", l1);
		
		List<String> l2 = new ArrayList<String>();
		l2.add("u4");l2.add("u5");
		l2 = removeDupsAndSelf(l2, "u2");
		dbLoader.loadUrlDb("u2", l2);
		
		List<String> l3 = new ArrayList<String>();
		l3.add("u1");l3.add("u2");
		l3 = removeDupsAndSelf(l3, "u3");
		dbLoader.loadUrlDb("u3", l3);
	}
	
	public void updateDB() {
			
		while(!Finished) {
			List<String> allKeys = dbLoader.getAllKeys();
			System.out.println("all Keys are ... " + allKeys);
			for(String keyUrl : allKeys) {
				System.out.println("the outlinks of key  " + keyUrl + " ...: " + dbLoader.getOutlinksByKey(keyUrl));
			}
			for(String keyUrl : allKeys) {
				List<String> outlinks = removeInvalidUrls(dbLoader.getOutlinksByKey(keyUrl));
				System.out.println("now the outlinks of key " + keyUrl + " ...: " + outlinks);			
				if(outlinks == null || outlinks.size() == 0)  {
					dbLoader.deleteUrlRecord(keyUrl);
				} else {
					dbLoader.loadUrlDb(keyUrl, outlinks);
				}				
			}
		}
		System.out.println("all Keys are ... " + dbLoader.getAllKeys());
		for(String keyUrl : dbLoader.getAllKeys()) {
			System.out.println("the outlinks of key  " + keyUrl + " ...: " + dbLoader.getOutlinksByKey(keyUrl));
		}
		
	}
	
	//remove duplicate urls and the key itself
	public List<String> removeDupsAndSelf(List<String> outlinks, String keyUrl) {	
		ArrayList<String> result = new ArrayList<String>();
		if(outlinks == null || outlinks.size() == 0) return result;
		
		for(String url : outlinks) {
			if(!result.contains(url) && !url.equals(keyUrl)) {
				result.add(url);
			}
		}		
		return result;
	}
	
	public List<String> removeInvalidUrls(List<String> outlinks) {
		List<String> result = new ArrayList<String>();
		boolean isChanged = false;
		if(outlinks == null || outlinks.size() == 0) return result;
		
		for(String url : outlinks) {
			if(dbLoader.checkUrlExists(url)) {
				result.add(url);
			} else {
				isChanged = true;
			}
		}
		if(!isChanged) Finished = true;
		return result;
	}
	
	public void deleteEntry(String keyUrl) {
		List<String> outlinks = dbLoader.getOutlinksByKey(keyUrl);
		if(outlinks == null) return;
		if(outlinks.size() == 0) {
			dbLoader.deleteUrlRecord(keyUrl);
		}
	}
	

	
}
