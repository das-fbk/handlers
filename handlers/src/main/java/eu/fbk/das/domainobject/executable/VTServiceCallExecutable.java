package eu.fbk.das.domainobject.executable;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.w3c.dom.Element;

import eu.fbk.das.domainobject.executable.utils.BotTelegram.TravelAssistantBot;
import eu.fbk.das.domainobject.executable.utils.Rome2Rio.TripAlternativeRome2Rio;
import eu.fbk.das.process.engine.api.AbstractExecutableActivityInterface;
import eu.fbk.das.process.engine.api.DomainObjectInstance;
import eu.fbk.das.process.engine.api.ProcessEngine;
import eu.fbk.das.process.engine.api.domain.ConcreteActivity;
import eu.fbk.das.process.engine.api.domain.ProcessActivity;
import eu.fbk.das.process.engine.api.domain.ProcessDiagram;
import eu.fbk.das.process.engine.api.jaxb.VariableType;

public class VTServiceCallExecutable extends
		AbstractExecutableActivityInterface {

	private static final Logger logger = LogManager
			.getLogger(VTServiceCallExecutable.class);

	private ProcessEngine pe;
	private JSONObject viaggiaTrentoJson;

	public VTServiceCallExecutable(ProcessEngine processEngine,
			ArrayList<TripAlternativeRome2Rio> alternatives,
			TravelAssistantBot bot) {
		this.pe = processEngine;
		this.viaggiaTrentoJson = new JSONObject();
	}

	@Override
	public void execute(ProcessDiagram proc, ProcessActivity pa) {
		ConcreteActivity currentConcrete = (ConcreteActivity) proc
				.getCurrentActivity();

		DomainObjectInstance doi = pe.getDomainObjectInstance(proc);

		// get the domain object state
		List<VariableType> doiState = doi.getState().getStateVariable();
		if (doiState != null) {

			logger.error("Domain Object with a state! ");
			// concrete logic

			Element from = doi.getStateVariableContentByName("From");
			Element to = doi.getStateVariableContentByName("To");
			String fromValue = from.getFirstChild().getNodeValue();
			String toValue = to.getFirstChild().getNodeValue();

			this.viaggiaTrentoJson = this.CallViaggiaTrento(fromValue, toValue);

			// update the PlannerOutput variable value
			Element jsonElement = doi
					.getStateVariableContentByName("PlannerOutput");
			jsonElement.setTextContent("ViaggiaTrento<>"
					+ this.viaggiaTrentoJson.toString());
			// save result in response variable

			doi.setStateVariableContentByVarName("PlannerOutput", jsonElement);

			// set activity to executed
			currentConcrete.setExecuted(true);
			return;
		}
		logger.debug("Domain Object without a state! ");
		currentConcrete.setExecuted(true);
		return;
	}

	private JSONObject CallViaggiaTrento(String from, String to) {
		// ViaggiaTrentoAPIWrapper viaggiaWrapper = new
		// ViaggiaTrentoAPIWrapper();

		JSONObject result = new JSONObject();

		// viaggiaWrapper.getViaggiaAlternatives(from, to);

		return result;
	}

	// returns the result of the API call as string
	public static String callURL(String myURL) {
		System.out.println(myURL);
		StringBuilder sb = new StringBuilder();
		URLConnection urlConn = null;
		InputStreamReader in = null;
		try {
			URL url = new URL(myURL);
			urlConn = url.openConnection();
			if (urlConn != null)
				urlConn.setReadTimeout(60 * 1000);
			if (urlConn != null && urlConn.getInputStream() != null) {
				in = new InputStreamReader(urlConn.getInputStream(),
						Charset.defaultCharset());
				BufferedReader bufferedReader = new BufferedReader(in);
				if (bufferedReader != null) {
					int cp;
					while ((cp = bufferedReader.read()) != -1) {
						sb.append((char) cp);
					}
					bufferedReader.close();
				}
			}
			in.close();
		} catch (Exception e) {
			// throw {new RuntimeException("Exception while calling URL:"+
			// myURL, e);
			return "erroreAPI";

		}

		return sb.toString();
	}

}
