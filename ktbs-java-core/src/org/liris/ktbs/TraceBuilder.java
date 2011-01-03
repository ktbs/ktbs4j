package org.liris.ktbs;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.liris.ktbs.core.Obsel;
import org.liris.ktbs.core.Relation;
import org.liris.ktbs.core.Trace;
import org.liris.ktbs.core.impl.KtbsResourceFactory;

public class TraceBuilder {

	private Trace trace;
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy kk:mm:ss");
	public TraceBuilder() {
		super();
	}


	private String traceModelUri;
	private String day;

	public void createNewTrace(String traceUri, String traceModelUri, String label, String day, String origin) {
		Date originDate;
		try {
			this.traceModelUri = traceModelUri;
			this.day = day;
			originDate = DATE_FORMAT.parse(day+" "+origin);
			trace = KtbsResourceFactory.createTrace(traceUri, traceModelUri, label, originDate, "http://localhost:8001/base1/", false);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public Obsel addInstantObsel(String beginend, String subject, String label, String type, Object... attributes) {
		return addObsel(beginend, beginend, subject, label, type, attributes);
	}

	public Relation addRelation(Obsel from, Obsel to, String relationName) {
		return KtbsResourceFactory.createRelation(from, traceModelUri+relationName, to);
	}

	public Obsel addObsel(String begin, String end, String subject, String label, String type, Object... attributes) {
		try {
			Date beginDate = DATE_FORMAT.parse(day+" " + begin);
			Date endDate = DATE_FORMAT.parse(day+" " + end);

			String typeUri = traceModelUri+type;

			Map<String, Object> attributeMap = new HashMap<String, Object>();
			String attributeName = null;
			for(int i=0;i<attributes.length;i++) {
				if(i%2==0) {
					attributeName = (String) attributes[i];
				} else {
					Object attributeValue = attributes[i];
					attributeMap.put(attributeName, attributeValue);
				}
			}

			Obsel obsel = KtbsResourceFactory.createObsel(trace, subject, label, beginDate, endDate, typeUri, attributeMap);
			trace.addObsel(obsel);
			return obsel;

		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Trace getTrace() {
		return trace;
	}
}
