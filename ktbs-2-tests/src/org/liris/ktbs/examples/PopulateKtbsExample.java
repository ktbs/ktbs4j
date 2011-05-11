package org.liris.ktbs.examples;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.liris.ktbs.client.Ktbs;
import org.liris.ktbs.client.KtbsClient;
import org.liris.ktbs.domain.interfaces.IAttributeType;
import org.liris.ktbs.domain.interfaces.IMethod;
import org.liris.ktbs.domain.interfaces.IObselType;
import org.liris.ktbs.domain.interfaces.IRelationType;
import org.liris.ktbs.domain.interfaces.ITraceModel;
import org.liris.ktbs.service.ResourceService;
import org.liris.ktbs.service.StoredTraceService;
import org.liris.ktbs.service.impl.ObselBuilder;
import org.liris.ktbs.utils.KtbsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PopulateKtbsExample {

	private static final Logger logger = LoggerFactory.getLogger(PopulateKtbsExample.class);

	private static ExampleTraceModel modelFiller = new ExampleTraceModel();

	private static String BASE_LOCAL_NAME = "base2";

	public static void main(String[] args) throws FileNotFoundException, IOException {
		logger.info("Classpath: " + System.getProperty("java.class.path"));

		if(args.length > 0 && args[0] instanceof String)
			BASE_LOCAL_NAME = args[0];

		KtbsClient ktbsClient = Ktbs.getRestClient();

		ResourceService service = ktbsClient.getResourceService();
		StoredTraceService traceService = ktbsClient.getStoredTraceService();


		if(ktbsClient == null || service == null || traceService == null) {
			logger.error("Could not start the KTBS client.");
			System.exit(1);
		}

		if(service.getBase(BASE_LOCAL_NAME) != null) {
			logger.error("The base {} already exists on the KTBS. Try with another base local name.", BASE_LOCAL_NAME);
			System.exit(1);
		}

		service.newBase(BASE_LOCAL_NAME, "Toto");
		String modelUri = service.newTraceModel(BASE_LOCAL_NAME, "model1");

		// Fill the trace model
		ITraceModel traceModel = service.getTraceModel(modelUri);
		modelFiller.fill(traceModel, Ktbs.getPojoFactory());
		service.saveResource(traceModel);

		String t01Uri = service.newStoredTrace(
				BASE_LOCAL_NAME, 
				"t01", 
				modelUri, 
				KtbsUtils.xsdDateUTC(2010, 4, 28, 18, 9, 0), 
		"Toto");

		// the count method
		Map<String, String> params = new HashMap<String, String>();
		params.put("script", IOUtils.toString(new FileReader("count.py")));
		String countMethodUri = service.newMethod(BASE_LOCAL_NAME, "count", IMethod.SCRIPT_PYTHON, params);

		// the hello world method
		params = new HashMap<String, String>();
		params.put("script", IOUtils.toString(new FileReader("helloworld.py")));
		String helloWorldMethodUri = service.newMethod(BASE_LOCAL_NAME, "helloworld", IMethod.SCRIPT_PYTHON, params);

		// the session method
		params = new HashMap<String, String>();
		params.put("sparql", IOUtils.toString(new FileReader("session.rq")));
		params.put("dummy", "\"dummy parameter\"");
		String sessionMethodUri = service.newMethod(BASE_LOCAL_NAME, "session", IMethod.SPARQL, params);

		// the computed trace filtered1
		params = new HashMap<String, String>();
		params.put("finish", "5000");
		Set<String> sourceTraces = new HashSet<String>();
		sourceTraces.add(t01Uri);
		String filtered1Uri = service.newComputedTrace(BASE_LOCAL_NAME, "filtered1", IMethod.FILTER, sourceTraces, params);

		// the computed trace filtered1
		params = new HashMap<String, String>();
		params.put("finish", "2000");
		sourceTraces = new HashSet<String>();
		sourceTraces.add(filtered1Uri);
		String filtered2Uri = service.newComputedTrace(BASE_LOCAL_NAME, "filtered2", IMethod.FILTER, sourceTraces, params);

		// the computed trace fusioned1
		sourceTraces = new HashSet<String>();
		sourceTraces.add(t01Uri);
		sourceTraces.add(filtered2Uri);
		String fusioned1Uri = service.newComputedTrace(BASE_LOCAL_NAME, "fusioned1", IMethod.FUSION, sourceTraces, null);

		// the computed trace helloword1
		String helloWorld1Uri = service.newComputedTrace(BASE_LOCAL_NAME, "helloworld1", helloWorldMethodUri, null, null);

		// the computed trace session1
		sourceTraces = new HashSet<String>();
		sourceTraces.add(t01Uri);
		String session1Uri = service.newComputedTrace(BASE_LOCAL_NAME, "session1", sessionMethodUri, sourceTraces, null);

		// the computed trace count1
		params = new HashMap<String, String>();
		params.put("window", "5000");
		sourceTraces = new HashSet<String>();
		sourceTraces.add(t01Uri);
		String count1Uri = service.newComputedTrace(BASE_LOCAL_NAME, "count1", countMethodUri, sourceTraces, params);

		// the computed trace session1
		sourceTraces = new HashSet<String>();
		sourceTraces.add(t01Uri);
		sourceTraces.add(filtered1Uri);
		sourceTraces.add(filtered2Uri);
		sourceTraces.add(fusioned1Uri);
		String count2Uri = service.newComputedTrace(BASE_LOCAL_NAME, "count2", countMethodUri, sourceTraces, null);


		// obsel1
		ObselBuilder obs = traceService.newObselBuilder(t01Uri);
		obs.setType((IObselType)traceModel.get("OpenChat"));
		obs.setEnd(1000);
		obs.setSubject("béa");
		obs.setBeginDT(KtbsUtils.xsdDateUTC(2010,4,28,18,9,1));
		obs.addAttribute((IAttributeType)traceModel.get("channel"), "#my-channel");
		String obs1Uri = obs.create();


		// obsel2
		obs = traceService.newObselBuilder(t01Uri);
		obs.setType((IObselType)traceModel.get("SendMsg"));
		obs.setBegin(2000);
		obs.setEnd(4000);
		obs.setSubject("béa");
		obs.addAttribute((IAttributeType)traceModel.get("message"), "hello world");
		obs.addRelation((IRelationType)traceModel.get("onChannel"), obs1Uri);
		String obs2Uri = obs.create();

		// obsel3
		obs = traceService.newObselBuilder(t01Uri);
		obs.setType((IObselType)traceModel.get("RecvMsg"));
		obs.setBegin(5000);
		obs.setEnd(5000);
		obs.setSubject("béa");
		obs.addAttribute((IAttributeType)traceModel.get("from"), "world");
		obs.addAttribute((IAttributeType)traceModel.get("message"), "hello world");
		obs.addRelation((IRelationType)traceModel.get("onChannel"), obs1Uri);
		String obs3Uri = obs.create();

		// obsel4
		obs = traceService.newObselBuilder(t01Uri);
		obs.setType((IObselType)traceModel.get("CloseChat"));
		obs.setBegin(7000);
		obs.setEndDT(KtbsUtils.xsdDateUTC(2010, 4, 28, 18, 9, 7));
		obs.setSubject("béa");
		obs.addRelation((IRelationType)traceModel.get("onChannel"), obs1Uri);
		String obs4Uri = obs.create();
	}
}
