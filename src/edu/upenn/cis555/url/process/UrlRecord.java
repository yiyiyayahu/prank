package edu.upenn.cis555.url.process;

import java.util.List;

public class UrlRecord {
	public String keyUrl;
	public List<String> outlinks;
	
	public String getKeyUrl() {
		return keyUrl;
	}
	public void setKeyUrl(String keyUrl) {
		this.keyUrl = keyUrl;
	}
	public List<String> getOutlinks() {
		return outlinks;
	}
	public void setOutlinks(List<String> outlinks) {
		this.outlinks = outlinks;
	}
	
}
