package org.liris.ktbs.tests;

import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.liris.ktbs.utils.KtbsUtils;

import com.hp.hpl.jena.datatypes.xsd.XSDDateTime;

public class XSDDateFormat {
	
	public static void main(String[] args) throws ParseException {
		Calendar cal = new GregorianCalendar();
		cal.setTime(Calendar.getInstance().getTime());
		XSDDateTime x = new XSDDateTime(cal);
		System.out.println(x.toString());
		System.out.println(KtbsUtils.now());
		System.out.println(KtbsUtils.parseXsdDate("2011-03-09T16:32:45.000Z"));
	}
}

