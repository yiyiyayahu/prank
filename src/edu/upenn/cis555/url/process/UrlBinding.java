package edu.upenn.cis555.url.process;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.bind.tuple.TupleOutput;

public class UrlBinding extends TupleBinding {

	@Override
	/**
	 * If the outlinks string is null or empty, then the outlinks of the urlRecord has size 0
	 */
	public Object entryToObject(TupleInput arg0) {
		String keyUrl = arg0.readString();
		String links = arg0.readString();
		List<String> outlinks = new ArrayList<String>();
		if(links != null || links.length() == 0) {
			outlinks= Arrays.asList(links.split("\n"));
		}
		
		UrlRecord urlRecord = new UrlRecord();
		urlRecord.setKeyUrl(keyUrl);
		urlRecord.setOutlinks(outlinks);
		return urlRecord;
	}

	/**
	 * If the outlinks of the urlRecord has size 0, then the string is empty ""
	 */
	@Override
	public void objectToEntry(Object arg0, TupleOutput arg1) {
		UrlRecord urlRecord = (UrlRecord)arg0;
		List<String> outlinks = urlRecord.getOutlinks();
		StringBuffer outLinksBuf = new StringBuffer();
		if(outlinks.size() > 0) {
			outLinksBuf.append(outlinks.get(0));
			for(int i = 1; i < outlinks.size(); i++) {
				outLinksBuf.append("\n" + outlinks.get(i));
			}
		} 
			
		arg1.writeString(urlRecord.getKeyUrl());
		arg1.writeString(outLinksBuf.toString());		
	}

}
