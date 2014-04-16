package edu.upenn.cis555.url.process;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class UrlFileWriter {
	private static DBLoader dbloader = new DBLoader();
	
	public static void main(String args[]) throws IOException {
		UrlFileWriter urlfw = new UrlFileWriter();
		urlfw.getData();
	}
	public void getData() throws IOException {
		List<String> keys = dbloader.getAllKeys();
		int length = keys.size();
		int fileNum = length/1000 + 1;
		createFiles(fileNum);
		for(int n = 0; n < fileNum; n++) {
			for(int i = n*1000; i < Math.min((n+1)*1000, length); i++) {
				File file = new File("/home/cis455/HadoopTest/url_test/" + "url" + n + ".txt");
				List<String> outlinks = dbloader.getOutlinksByKey(keys.get(i));
				writeToFile(keys.get(i), outlinks, file);
			}
		}
	}

	public void createFiles(int num) throws IOException {
		String dir = "/home/cis455/HadoopTest/url_test/";
		for(int i = 0; i < num; i++) {
			File file = new File(dir, "url" + i + ".txt");
			if(file.exists()) file.createNewFile();
		}
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
