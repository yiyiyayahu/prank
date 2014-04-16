package edu.upenn.cis555.url.process;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.je.Cursor;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;
import com.sleepycat.persist.EntityCursor;

public class DBLoader {
	private static File myDbsPath = new File("/home/cis455/URLDB/");

    // DatabaseEntries used for loading records
    private static DatabaseEntry key = new DatabaseEntry();
    private static DatabaseEntry data = new DatabaseEntry();

    private static MyDbs myDbs = new MyDbs();
    private static TupleBinding urlBinding;
    
    public DBLoader() {
    	init();
    }

    public void init() {
    	myDbs.setup(myDbsPath, false);
    	urlBinding = new UrlBinding();
        System.out.println("init finished....");
    }
    
    public void loadUrlDb(String keyUrl, List<String> outlinks) throws DatabaseException {
    	UrlRecord urlRecord = new UrlRecord();
    	urlRecord.setKeyUrl(keyUrl);
    	urlRecord.setOutlinks(outlinks);
    	
    	try {
    		key = new DatabaseEntry(keyUrl.getBytes("UTF-8"));
    	} catch(IOException e) { };
    	
    	urlBinding.objectToEntry(urlRecord, data);
    	myDbs.getUrlDB().put(null, key, data);
    }
    
    public void deleteUrlRecord(String keyUrl) {
    	System.out.println("deleting urls ... ");
    	try {
    		key = new DatabaseEntry(keyUrl.getBytes("UTF-8"));
    	} catch(IOException e) {}
    	myDbs.getUrlDB().delete(null, key);
    }
    
    public UrlRecord getUrlRecord(String keyUrl) {
    	try {
    		DatabaseEntry foundKey = new DatabaseEntry(keyUrl.getBytes("UTF-8"));
    		DatabaseEntry foundData = new DatabaseEntry();
    		if(myDbs.getUrlDB().get(null, foundKey, foundData, LockMode.DEFAULT)
    				== OperationStatus.SUCCESS) {
    			UrlRecord theUrl = (UrlRecord)urlBinding.entryToObject(foundData);
    			return theUrl;
    		} else {
    			System.out.println("didn't find such an entry with key: " + keyUrl);
    			return null;
    		}
    	} catch (Exception e) {
            System.err.println("Error on getUrlRecord:");
        }
    	return null;
    }
    
    public List<String> getOutlinksByKey(String keyUrl) {
    	UrlRecord urlRecord = getUrlRecord(keyUrl);
    	if(urlRecord == null) return null;
    	return urlRecord.outlinks;
    }
    
    public boolean checkUrlExists(String url) {
    	return (getUrlRecord(url) != null);
    }
    
    public List<String> getAllKeys() {
		List<String> urlList = new ArrayList<String>();
		Cursor urlCursor = myDbs.getUrlDB().openCursor(null, null);
		try {
			DatabaseEntry foundKey = new DatabaseEntry();
			DatabaseEntry foundData = new DatabaseEntry();
			while (urlCursor.getNext(foundKey, foundData, LockMode.DEFAULT) ==
			        OperationStatus.SUCCESS) {
				urlList.add(new String(foundKey.getData()));
			}
		} finally {
			urlCursor.close();
		}
		return urlList;
    }
    
	public void deleteFiles() {
		for(String s : myDbsPath.list()) {
			File file = new File(myDbsPath.getPath(), s);
			file.delete();
		}
	}

}
