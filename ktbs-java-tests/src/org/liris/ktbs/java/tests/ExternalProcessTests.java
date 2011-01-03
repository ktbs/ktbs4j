package org.liris.ktbs.java.tests;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ExternalProcessTests {

	private static final String KTBS_FULL = "/home/dcram/Applications/ktbs/bin/ktbs-full";

	public static void main(String[] args) throws Exception {

		String[] commandArray = {KTBS_FULL};
		String[] environment = {};

		Runtime runtime = Runtime.getRuntime();
		Process javap = runtime.exec(commandArray, environment);
		Thread.sleep(1000l);
		writeProcessOutput(javap);
	}

	static void writeProcessOutput(Process process) throws Exception{
		InputStreamReader tempReader = new InputStreamReader(new BufferedInputStream(process.getInputStream()));
		BufferedReader reader = new BufferedReader(tempReader);
		String line = null;

		while ((line=reader.readLine())!=null){
			System.out.println(line);
		}		
	}

	//	public static void main(String[] args) {
	//		 try {
	//		      String line;
	//		      Process p = Runtime.getRuntime().exec("/home/dcram/Applications/ktbs/bin/ktbs-full");
	//		      BufferedReader input =new BufferedReader(new InputStreamReader(p.getInputStream()));
	//		      while ((line = input.readLine()) != null) {
	//		        System.out.println(line);
	//		      }
	//		      input.close();
	//		    }
	//		    catch (Exception err) {
	//		      err.printStackTrace();
	//		    }
	//	}
}
