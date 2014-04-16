package edu.upenn.cis555.url.process;

import java.util.ArrayList;
import java.util.List;

public class rankChecker {
	public static DBLoader dbloader = new DBLoader();
	
	public static String checkUrl = "http://zeleandro98.tumblr.com/";
	public static List<Integer> outlinkSize = new ArrayList<Integer>();
	
	public static void getOutLinkSize() {
		System.out.println(dbloader.getOutlinksByKey(checkUrl));
		List<String> keyUrls = dbloader.getAllKeys();
		for(String keyUrl : keyUrls) {
			List<String> outlinks = dbloader.getOutlinksByKey(keyUrl);
			if(outlinks.contains(checkUrl)) {
				outlinkSize.add(outlinks.size());
			}
		}
		System.out.println(outlinkSize);
	}
	
	
	public static void main(String args[]) {
		double sum = 0;
		getOutLinkSize();
		for(int size : outlinkSize) {
			sum += 1.0/size;
		}
		sum = sum * 0.85 + 0.15;
		System.out.println(sum);
	}
	
}
