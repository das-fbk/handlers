package eu.fbk.das.domainobject.executable.test;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Element;

import eu.fbk.das.domainobject.executable.utils.BotTelegram.TravelAssistantBot;
import eu.fbk.das.domainobject.executable.utils.Rome2Rio.Segment;
import eu.fbk.das.domainobject.executable.utils.Rome2Rio.TripAlternativeRome2Rio;
import eu.fbk.das.process.engine.api.AbstractExecutableActivityInterface;
import eu.fbk.das.process.engine.api.DomainObjectInstance;
import eu.fbk.das.process.engine.api.ProcessEngine;
import eu.fbk.das.process.engine.api.domain.ConcreteActivity;
import eu.fbk.das.process.engine.api.domain.ProcessActivity;
import eu.fbk.das.process.engine.api.domain.ProcessDiagram;
import eu.fbk.das.process.engine.api.jaxb.VariableType;

public class DVMDefineDataPatternExecutable extends
		AbstractExecutableActivityInterface {

	private static final Logger logger = LogManager
			.getLogger(Rome2RioCallExecutable.class);

	private ProcessEngine pe;
	private TravelAssistantBot bot;

	// end bot elements

	public DVMDefineDataPatternExecutable(ProcessEngine processEngine,
			TravelAssistantBot bot) {
		this.pe = processEngine;
		this.bot = bot;

	}

	@Override
	public void execute(ProcessDiagram proc, ProcessActivity pa) {
		ConcreteActivity currentConcrete = (ConcreteActivity) proc
				.getCurrentActivity();

		long startParsingTime = System.nanoTime();
		long serviceExecTime = 0L;

		DomainObjectInstance doi = pe.getDomainObjectInstance(proc);

		// get the domain object state
		List<VariableType> doiState = doi.getState().getStateVariable();
		if (doiState != null) {

			logger.info("Domain Object with a state! ");
			// concrete activity logic
			DomainObjectInstance user = pe.getReferringUser(doi);
			// read plannerOutput value
			Element planOut = doi
					.getStateVariableContentByName("PlannerOutput");
			String planOutValue = planOut.getFirstChild().getNodeValue();

			// extract the current mobility Service (i.e., Rome2Rio,BlaBlaCar,
			// etc..
			if (planOutValue != null && !planOutValue.isEmpty()) {
				String[] parts = planOutValue.split("<>");
				String service = parts[0];
				String PlannerOutputValue = parts[1];
				if (service.equalsIgnoreCase("Rome2Rio")) {
					JSONObject jsonObj = new JSONObject(PlannerOutputValue);

					String planList = extractPlanListOfRome2Rio(jsonObj);

					// update the PlanList variable value
					Element planElement = doi
							.getStateVariableContentByName("PlanList");
					planElement.setTextContent(planList);
					// save result in response variable
					doi.setStateVariableContentByVarName("PlanList",
							planElement);
					// memorize the DataPattern in to the variable
					Element dataPatternElement = doi
							.getStateVariableContentByName("DataPattern");
					dataPatternElement.setTextContent("Rome2Rio");
					// save result in response variable
					doi.setStateVariableContentByVarName("DataPattern",
							dataPatternElement);

					serviceExecTime = System.nanoTime() - startParsingTime;
					pe.setTestServicesLog(user.getId(), "DataViewer_R2R",
							serviceExecTime, 0, 0);

				} else if (service.equalsIgnoreCase("BlaBlaCar")) {
					// update the PlanList variable value
					Element planElement = doi
							.getStateVariableContentByName("PlanList");
					planElement.setTextContent(PlannerOutputValue);
					// save result in response variable
					doi.setStateVariableContentByVarName("PlanList",
							planElement);

					// memorize the DataPattern in to the variable
					Element dataPatternElement = doi
							.getStateVariableContentByName("DataPattern");
					dataPatternElement.setTextContent("BlaBlaCar");
					// save result in response variable
					doi.setStateVariableContentByVarName("DataPattern",
							dataPatternElement);

					serviceExecTime = System.nanoTime() - startParsingTime;
					pe.setTestServicesLog(user.getId(), "DataViewer_BBC",
							serviceExecTime, 0, 0);

				} else if (service.equalsIgnoreCase("ViaggiaTrento")) {
					// update the PlanList variable value
					Element planElement = doi
							.getStateVariableContentByName("PlanList");
					planElement.setTextContent(PlannerOutputValue);
					// save result in response variable
					doi.setStateVariableContentByVarName("PlanList",
							planElement);

					// memorize the DataPattern in to the variable
					Element dataPatternElement = doi
							.getStateVariableContentByName("DataPattern");
					dataPatternElement.setTextContent("ViaggiaTrento");
					// save result in response variable
					doi.setStateVariableContentByVarName("DataPattern",
							dataPatternElement);

					serviceExecTime = System.nanoTime() - startParsingTime;

					pe.setTestServicesLog(user.getId(), "DataViewer_VT",
							serviceExecTime, 0, 0);
				}
			} else {
				Element planElement = doi
						.getStateVariableContentByName("PlanList");
				planElement.setTextContent("");
				// save result in response variable
				doi.setStateVariableContentByVarName("PlanList", planElement);

				// memorize the DataPattern in to the variable
				Element dataPatternElement = doi
						.getStateVariableContentByName("DataPattern");
				dataPatternElement.setTextContent("");
				// save result in response variable
				doi.setStateVariableContentByVarName("DataPattern",
						dataPatternElement);
			}

			// set activity to executed
			currentConcrete.setExecuted(true);
			return;
		}

		currentConcrete.setExecuted(true);
		return;
	}

	private String extractPlanListOfRome2Rio(JSONObject plannerOutputValue) {

		ArrayList<TripAlternativeRome2Rio> alternatives = new ArrayList<TripAlternativeRome2Rio>();
		String planListResult = null;

		JSONArray routes = new JSONArray();
		routes = plannerOutputValue.getJSONArray("routes");
		for (int i = 0; i < routes.length(); i++) {

			System.out.println("Soluzione " + i + ":");
			JSONObject route = (JSONObject) routes.get(i);
			System.out.println(route);
			// transportation mean
			String mean = route.getString("name");

			// PRICES
			JSONArray prices = new JSONArray();
			JSONObject price = new JSONObject();
			Double priceInd = 0.0;
			if (route.has("indicativePrices")) {
				prices = route.getJSONArray("indicativePrices");
			}

			if (prices != null && prices.length() != 0) {
				price = (JSONObject) prices.get(0);
				// price
				priceInd = price.getDouble("price");
			}

			// distance
			Double distance = route.getDouble("distance");

			// duration
			Double duration = route.getDouble("totalDuration");

			// places
			JSONArray places = new JSONArray();
			places = plannerOutputValue.getJSONArray("places");

			// vehicles
			JSONArray vehicles = new JSONArray();
			vehicles = plannerOutputValue.getJSONArray("vehicles");

			// agencies
			JSONArray agencies = new JSONArray();
			agencies = plannerOutputValue.getJSONArray("agencies");

			// from
			// int from = (int) route.getLong("depPlace");

			// JSONObject placeFrom = places.getJSONObject(from);
			// double fromLat = placeFrom.getDouble("lat");
			// double fromLng = placeFrom.getDouble("lng");

			// to
			// int to = (int) route.getLong("arrPlace");
			// JSONObject placeTo = places.getJSONObject(to);
			// double toLat = placeTo.getDouble("lat");
			// double toLng = placeTo.getDouble("lng");

			// changes
			JSONArray changes = new JSONArray();
			changes = route.getJSONArray("segments");
			int number_changes = -1;
			for (int j = 0; j < changes.length(); j++) {
				number_changes++;
			}

			// Segments Creation
			ArrayList<Segment> segments = new ArrayList<Segment>();
			JSONArray segmentsArray = new JSONArray();
			segmentsArray = route.getJSONArray("segments");

			for (int k = 0; k < segmentsArray.length(); k++) {
				JSONObject segment = (JSONObject) segmentsArray.get(k);
				System.out.println("SEGMENT " + segment);
				int segmentFrom = segment.getInt("depPlace");
				int segmentTo = segment.getInt("arrPlace");
				int segmentVehicle = segment.getInt("vehicle");

				JSONArray agencyArray = new JSONArray();
				int agencyNumber = 999;
				if (segment.has("agencies")) {
					agencyArray = segment.getJSONArray("agencies");
					JSONObject agency = (JSONObject) agencyArray.get(0);
					agencyNumber = agency.getInt("agency");
				}

				// search From
				String fromValue = retrievePlace(segmentFrom, places);
				// search To
				String toValue = retrievePlace(segmentTo, places);
				// search Vehicle
				String vehicleValue = retrieveVehicle(segmentVehicle, vehicles);
				// search Agency
				String agencyValue = retrieveAgency(agencyNumber, agencies);

				// create and add the segment to the list of segments
				Segment newSegment = new Segment(vehicleValue, agencyValue,
						fromValue, toValue);
				segments.add(newSegment);
			}
			TripAlternativeRome2Rio alternative = new TripAlternativeRome2Rio(
					i, mean, priceInd, distance, duration, segments,
					number_changes);
			alternatives.add(alternative);

			// save alternatives to be used later by the bot
			// bot.setRomeToRioAlternatives(alternatives);

		}

		planListResult = extractString(alternatives);
		return planListResult;
	}

	private String extractString(ArrayList<TripAlternativeRome2Rio> alternatives) {
		// extract unique String from the tripAlternatives
		String result = "";
		if (alternatives.size() != 0) {
			for (int i = 0; i < alternatives.size(); i++) {
				String current = alternatives.get(i).getMean();
				if (i == 0) {
					result = i + "," + current + "-";
				} else if (i == alternatives.size() - 1) {
					result = result + i + "," + current;
				} else {
					result = result + i + "," + current + "-";
				}
			}
		}
		return result;
	}

	private String retrieveAgency(int index, JSONArray agencies) {
		String result = "";
		if (index == 999) {
			result = "999";
		} else {
			JSONObject agency = (JSONObject) agencies.get(index);
			result = agency.getString("name");
		}
		return result;
	}

	private String retrievePlace(int index, JSONArray places) {
		String result = "";
		JSONObject place = (JSONObject) places.get(index);
		result = place.getString("shortName");
		return result;
	}

	private String retrieveVehicle(int index, JSONArray vehicles) {
		String result = "";
		JSONObject vehicle = (JSONObject) vehicles.get(index);
		result = vehicle.getString("name");
		return result;
	}

}
