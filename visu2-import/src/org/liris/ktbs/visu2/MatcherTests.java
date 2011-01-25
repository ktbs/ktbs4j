package org.liris.ktbs.visu2;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;



public class MatcherTests {

	/**
	 * @param args
	 * @throws URISyntaxException 
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws URISyntaxException, FileNotFoundException, IOException {
		
		
		String regex = "^(\\s*\\.\\s+(a))\\s+";
		System.out.println(regex);
		System.out.println("*********************************");
		Pattern p = Pattern.compile("^(\\s*\\.\\s+(a))\\s+", Pattern.MULTILINE);
		String string = IOUtils.toString(new FileInputStream("/home/dcram/Documents/trace-visu/traces/visu2-absolute/trace-20101206152020-7.ttl"), "ISO-8859-1");
		Matcher m = p.matcher(string);
		
		while (m.find()) {
			System.out.println("--------------------------");
			String g = m.group();
			
			System.out.println(g + "[" + m.start() + "-" + m.end() + "]");
			
		}
	}

}
