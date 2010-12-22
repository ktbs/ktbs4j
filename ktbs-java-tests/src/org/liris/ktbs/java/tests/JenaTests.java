package org.liris.ktbs.java.tests;

import com.ibm.icu.util.Calendar;

public class JenaTests {
	public static void main(String[] args) {

		Calendar start = Calendar.getInstance();
//		Model model = ModelFactory.createDefaultModel(ReificationStyle);
		Calendar end = Calendar.getInstance();

		System.out.println(end.getTimeInMillis()-start.getTimeInMillis());

	}
}
