package eu.fbk.das.domainobject.executable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Element;

import eu.fbk.das.domainobject.executable.utils.TripAlternative;
import eu.fbk.das.domainobject.executable.utils.BlaBlaCar.BlaBlaCarAPIWrapper;
import eu.fbk.das.domainobject.executable.utils.BotTelegram.TravelAssistantBot;
import eu.fbk.das.process.engine.api.AbstractExecutableActivityInterface;
import eu.fbk.das.process.engine.api.DomainObjectInstance;
import eu.fbk.das.process.engine.api.ProcessEngine;
import eu.fbk.das.process.engine.api.domain.ConcreteActivity;
import eu.fbk.das.process.engine.api.domain.ProcessActivity;
import eu.fbk.das.process.engine.api.domain.ProcessDiagram;
import eu.fbk.das.process.engine.api.jaxb.VariableType;

public class BBCServiceCallExecutable extends
		AbstractExecutableActivityInterface {

	private static final Logger logger = LogManager
			.getLogger(Rome2RioCallExecutable.class);

	private ProcessEngine pe;
	private ArrayList<TripAlternative> alternatives;
	private TravelAssistantBot bot;
	private JSONObject rome2rioJson;

	private static int hoaaCounter = 1;

	public BBCServiceCallExecutable(ProcessEngine processEngine,
			TravelAssistantBot bot) {
		this.pe = processEngine;
		this.alternatives = alternatives;
		this.bot = bot;
		this.rome2rioJson = new JSONObject();
	}

	@Override
	public void execute(ProcessDiagram proc, ProcessActivity pa) {
		ConcreteActivity currentConcrete = (ConcreteActivity) proc
				.getCurrentActivity();

		DomainObjectInstance doi = pe.getDomainObjectInstance(proc);
		ProcessDiagram process = doi.getProcess();

		// get the domain object state
		List<VariableType> doiState = doi.getState().getStateVariable();
		if (doiState != null) {

			logger.error("Domain Object with a state! ");
			// concrete logic

			Element source = doi.getStateVariableContentByName("Source");
			Element destination = doi
					.getStateVariableContentByName("Destination");
			String sourceValue = source.getFirstChild().getNodeValue();
			String destinationValue = destination.getFirstChild()
					.getNodeValue();

			// call BlaBlaCar Wrapped Service
			BlaBlaCarAPIWrapper blablaWrapper = new BlaBlaCarAPIWrapper();
			JSONObject result = null;
			try {
				result = blablaWrapper.getBlaBlaAlternatives(sourceValue,
						destinationValue);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(result);
			// prendi il link dal risultato
			JSONArray trips = new JSONArray();
			trips = result.getJSONArray("trips");

			// prendo il primo ride
			JSONObject trip = (JSONObject) trips.get(0);
			System.out.println(trip);
			// ride links
			JSONObject links = (JSONObject) trip.get("links");
			System.out.println(links);
			String front = (String) links.get("_front");
			System.out.println("front: " + front);

			// update the ResultList variable value
			Element jsonElement = doi
					.getStateVariableContentByName("ResultList");
			jsonElement.setTextContent("BlaBlaCar<>" + front);
			// save result in response variable
			doi.setStateVariableContentByVarName("ResultList", jsonElement);

			// set activity to executed
			currentConcrete.setExecuted(true);
			return;
		}
		logger.debug("Domain Object without a state! ");
		currentConcrete.setExecuted(true);
		return;
	}
}
