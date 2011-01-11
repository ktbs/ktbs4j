package org.liris.ktbs.java.tests;

import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.hp.hpl.jena.datatypes.xsd.XSDDateTime;
import com.ibm.icu.text.SimpleDateFormat;

public class XsdDateTime {

	/**
	 * @param args
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws ParseException {
		Calendar cal = new GregorianCalendar();
		cal.set(Calendar.DAY_OF_MONTH, 10);
		cal.set(Calendar.YEAR, 2011);
		cal.set(Calendar.MONTH, Calendar.JANUARY);
		cal.set(Calendar.HOUR_OF_DAY, 17);
		cal.set(Calendar.MINUTE, 30);
		cal.set(Calendar.SECOND, 45);
		XSDDateTime x = new XSDDateTime(cal);
		System.out.println(cal.getTime());
		System.out.println(x.asCalendar().getTime());
		

		System.out.println(new SimpleDateFormat("yyyy-MM-dd kk:mm:ss").parse("2010-04-28 20:09:01"));
	}

}
