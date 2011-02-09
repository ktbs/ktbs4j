package org.liris.ktbs.visu2;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Visu2Utils {


	public static String fixTurtle(String turtle) {
		String s = turtle;
		for(Entry<String, String> replacement:Visu2Utils.getFixRegexs().entrySet()) {
			Pattern p = Pattern.compile(replacement.getKey(), Pattern.MULTILINE);
			Matcher m = p.matcher(s);
			s = m.replaceAll(replacement.getValue());
		}
		return s;
	}
	
	public static Map<String, String> getFixRegexs() {
		Map<String, String> replacements = new HashMap<String, String>();

		/* 
		 * (MULTILINE)
		 * FROM:
		 * . a 
		 * 
		 * TO:
		 * [] a 
		 */
		replacements.put("^\\s*\\.\\s+a\\s+", "[] a ");
		
		/* 
		 * (MULTILINE)
		 * FROM:
		 * :hasContent "identité nati
			onal";
		 * 
		 * TO:
		 * :hasContent "identité national"; 
		 */
		replacements.put("nati\nonal", "national");


		/* 
		 * (MULTILINE)
		 * FROM:
		 * :hasText "Couverture de la bande dessinée "Bienvenue à Boboland"";
		 * 
		 * TO:
		 * :hasText  """Couverture de la bande dessinée "Bienvenue à Boboland"""";
		 * 
		 */
		replacements.put("^\\s*(:\\S+)\\s+\"(.*\".*)\"\\s*;", "$1 \"\"\"\n$2\n\"\"\";");
		
		/* 
		 * FROM "" TO "'"
		 */
		replacements.put("\\x92", "'");
		
		
		/* 
		 * FROM a tab character "\t" (not allowed in literals by the TURTLE parser of the KTBS) TO a whitespace character " ".
		 */
		replacements.put("\t", " ");

		
		/* 
		 * FROM
		 * 
		 * <http://liris.cnrs.fr/silex/2009/ktbs/>
		 * 
		 * TO
		 * 
		 * <http://liris.cnrs.fr/silex/2009/ktbs#>
		 */
		replacements.put("<http://liris.cnrs.fr/silex/2009/ktbs/>", "<http://liris.cnrs.fr/silex/2009/ktbs#>");
		
		return replacements;
	}
}
