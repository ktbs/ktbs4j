package org.liris.ktbs.utils;

import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RelativeURITurtleReader {

	public String resolve(String inputString, String base) {
		try {

			URI baseURI = new URI(base);

			// Create a pattern to match cat
			Pattern pattern = Pattern.compile("<.*>");
			Matcher m = pattern.matcher(inputString);
			// Create a matcher with an input string

			StringBuffer sb = new StringBuffer();
			// Loop through and create a new String 
			// with the replacements
			while(m.find()) {
				String group = m.group().replaceFirst("<", "").replaceFirst(">", "");

				URI uri = new URI(group);
				if(!uri.isAbsolute()) 
					uri = baseURI.resolve(uri);

				m.appendReplacement(sb, "<" + uri.toString() + ">");
			}
			// Add the last segment of input to 
			// the new String
			m.appendTail(sb);

			return  sb.toString();
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
}