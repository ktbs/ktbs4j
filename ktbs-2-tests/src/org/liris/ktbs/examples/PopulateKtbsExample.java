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
import org.liris.ktbs.client.KtbsRootClient;
import org.liris.ktbs.domain.interfaces.IAttributeType;
import org.liris.ktbs.domain.interfaces.IMethod;
import org.liris.ktbs.domain.interfaces.IObselType;
import org.liris.ktbs.domain.interfaces.IRelationType;
import org.liris.ktbs.domain.interfaces.ITraceModel;
import org.liris.ktbs.service.ResourceService;
import org.liris.ktbs.service.StoredTraceService;
import org.liris.ktbs.service.impl.ObselBuilder;
import org.liris.ktbs.utils.KtbsUtils;

public class PopulateKtbsExample {
	
	private static ExampleTraceModel modelFiller = new ExampleTraceModel();
	
	public static void main(String[] args) throws FileNotFoundException, IOException {
		KtbsRootClient ktbsClient = Ktbs.getRestRootClient();
		
		ResourceService service = ktbsClient.getResourceService();
		StoredTraceService traceService = ktbsClient.getStoredTraceService();
		
		
		service.newBase("base1", "Toto");
		String modelUri = service.newTraceModel("base1", "model1");
		
		// Fill the trace model
		ITraceModel traceModel = service.getTraceModel(modelUri);
		modelFiller.fill(traceModel, Ktbs.getPojoFactory());
		service.saveResource(traceModel);
		
		String t01Uri = service.newStoredTrace("base1", "t01", modelUri, KtbsUtils.now(), "Toto");
		
		// the count method
		Map<String, String> params = new HashMap<String, String>();
		params.put("script", IOUtils.toString(new FileReader("count.py")));
		String countMethodUri = service.newMethod("base1", "count", IMethod.SCRIPT_PYTHON, params);

		
		// the hello world method
		params = new HashMap<String, String>();
		params.put("script", IOUtils.toString(new FileReader("helloworld.py")));
		String helloWorldMethodUri = service.newMethod("base1", "helloworld", IMethod.SCRIPT_PYTHON, params);
		
		// the session method
		params = new HashMap<String, String>();
		params.put("script", IOUtils.toString(new FileReader("helloworld.py")));
		params.put("dummy", "\"dummy parameter\"");
		String sessionMethodUri = service.newMethod("base1", "session", IMethod.SPARQL, params);
		
		// the computed trace filtered1
		params = new HashMap<String, String>();
		params.put("finish", "5000");
		Set<String> sourceTraces = new HashSet<String>();
		sourceTraces.add(t01Uri);
		String filtered1Uri = service.newComputedTrace("base1", "filtered1", IMethod.FILTER, sourceTraces, params);
		
		// the computed trace filtered1
		params = new HashMap<String, String>();
		params.put("finish", "2000");
		sourceTraces = new HashSet<String>();
		sourceTraces.add(filtered1Uri);
		String filtered2Uri = service.newComputedTrace("base1", "filtered2", IMethod.FILTER, sourceTraces, params);
		
		// the computed trace fusioned1
		sourceTraces = new HashSet<String>();
		sourceTraces.add(t01Uri);
		sourceTraces.add(filtered2Uri);
		String fusioned1Uri = service.newComputedTrace("base1", "fusioned1", IMethod.FUSION, sourceTraces, null);
		
		// the computed trace helloword1
		String helloWorld1Uri = service.newComputedTrace("base1", "helloworld1", helloWorldMethodUri, null, null);
		
		// the computed trace session1
		sourceTraces = new HashSet<String>();
		sourceTraces.add(t01Uri);
		String session1Uri = service.newComputedTrace("base1", "session1", sessionMethodUri, sourceTraces, null);

		// the computed trace count1
		params = new HashMap<String, String>();
		params.put("window", "5000");
		sourceTraces = new HashSet<String>();
		sourceTraces.add(t01Uri);
		String count1Uri = service.newComputedTrace("base1", "count1", countMethodUri, sourceTraces, params);

		// the computed trace session1
		sourceTraces = new HashSet<String>();
		sourceTraces.add(t01Uri);
		sourceTraces.add(filtered1Uri);
		sourceTraces.add(filtered2Uri);
		sourceTraces.add(fusioned1Uri);
		String count2Uri = service.newComputedTrace("base1", "count2", countMethodUri, sourceTraces, null);
		
		
		// obsel1
		ObselBuilder obs = traceService.newObselBuilder(t01Uri);
		obs.setType((IObselType)traceModel.get("OpenChat"));
		obs.setEnd(1000);
		obs.setSubject("béa");
		obs.setBeginDT(KtbsUtils.xsdDate(2010,4,28,18,9,1));
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
		obs.setEndDT(KtbsUtils.xsdDate(2010, 4, 28, 18, 9, 7));
		obs.setSubject("béa");
		obs.addRelation((IRelationType)traceModel.get("onChannel"), obs1Uri);
		String obs4Uri = obs.create();
	}
}
