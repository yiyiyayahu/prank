package edu.upenn.cis555.url.process;
import java.io.File;

import com.sleepycat.bind.serial.StoredClassCatalog;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;

public class MyDbs {
	private Environment myEnv;
	private Database urlDb;	
	private Database classCatalogDb;	
    private StoredClassCatalog classCatalog;
		
	public MyDbs() {
			
	}
	
	public void setup(File envHome, boolean readOnly) throws DatabaseException {

        EnvironmentConfig myEnvConfig = new EnvironmentConfig();
        DatabaseConfig myDbConfig = new DatabaseConfig();

        myEnvConfig.setReadOnly(readOnly);
        myDbConfig.setReadOnly(readOnly);
        
	    // the environment is opened for write
        myEnvConfig.setAllowCreate(!readOnly);
        myDbConfig.setAllowCreate(!readOnly);

        // Allow transactions
        myEnvConfig.setTransactional(!readOnly);
        myDbConfig.setTransactional(!readOnly);

	    // Open the environment
	    myEnv = new Environment(envHome, myEnvConfig);

	    urlDb = myEnv.openDatabase(null, "UrlDB", myDbConfig);
	    
        // Open the class catalog db. This is used to optimize class serialization.
	    classCatalogDb = myEnv.openDatabase(null, "ClassCatalogDB", myDbConfig);

        // Create our class catalog
        classCatalog = new StoredClassCatalog(classCatalogDb);
	}
	
	public Database getUrlDB() {
		return urlDb;
	}
	
    public StoredClassCatalog getClassCatalog() {
        return classCatalog;
    }
    
    public void close() {
    	if(myEnv != null) {
	    	try{
	    		urlDb.close();
	    		classCatalogDb.close();
	    		myEnv.close();
			}catch(DatabaseException dbe) {
	            System.err.println("Error closing MyDbs: " + dbe.toString());
	            System.exit(-1);
			}
    	}
    }
}
