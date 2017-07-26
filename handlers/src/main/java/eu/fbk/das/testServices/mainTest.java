package eu.fbk.das.testServices;

import static java.util.concurrent.TimeUnit.DAYS;
import static java.util.concurrent.TimeUnit.HOURS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONObject;

import eu.fbk.das.domainobject.executable.utils.BlaBlaCar.BlaBlaCarAPIWrapper;
import eu.fbk.das.domainobject.executable.utils.BlaBlaCar.TripAlternativeBlaBlaCar;
import eu.fbk.das.domainobject.executable.utils.CityBikesAPI.CityBikesAPIWrapper;
import eu.fbk.das.domainobject.executable.utils.CityBikesAPI.CityBikesStation;
import eu.fbk.das.domainobject.executable.utils.GoogleAPI.GoogleAPIWrapper;
import eu.fbk.das.domainobject.executable.utils.GoogleAPI.GoogleTransitAlternative;
import eu.fbk.das.domainobject.executable.utils.LondonJourneyPlanner.LondonJPAPIWrapper;
import eu.fbk.das.domainobject.executable.utils.LondonJourneyPlanner.TripAlternativeLondonJP;
import eu.fbk.das.domainobject.executable.utils.Rome2Rio.Rome2RioAPIWrapper;
import eu.fbk.das.domainobject.executable.utils.Rome2Rio.TripAlternativeRome2Rio;
import eu.fbk.das.domainobject.executable.utils.ViaggiaTrento.TravelViaggiaTrento;
import eu.fbk.das.domainobject.executable.utils.ViaggiaTrento.ViaggiaTrentoAPIWrapper;

public class mainTest {

	private static final List<ExperimentResult> servicesCall = new ArrayList<ExperimentResult>();
	private static final int numOfRuns = 101;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("TEST SERVICES STARTED");
		for (int i = 0; i < numOfRuns; i++) {
			// ROME2RIO
			ExperimentResult resultRome2Rio = new ExperimentResult();
			resultRome2Rio = callRome2Rio();
			resultRome2Rio.setId(i);
			servicesCall.add(resultRome2Rio);

			// LONDON JOURNEY PLANNER
			ExperimentResult resultLondonJP = new ExperimentResult();
			resultLondonJP = callLondonJP();
			resultLondonJP.setId(i);
			servicesCall.add(resultLondonJP);

			// GOOGLE TRANSIT
			ExperimentResult resultGoogleTransit = new ExperimentResult();
			resultGoogleTransit = callGoogleTransit();
			resultGoogleTransit.setId(i);
			servicesCall.add(resultGoogleTransit);

			// VIAGGIA TRENTO
			ExperimentResult resultViaggiaTrento = new ExperimentResult();
			resultViaggiaTrento = callViaggiaTrento();
			resultViaggiaTrento.setId(i);
			servicesCall.add(resultViaggiaTrento);

			// BLABLACAR
			ExperimentResult resultBlaBlaCar = new ExperimentResult();
			resultBlaBlaCar = callBlaBlaCar();
			resultBlaBlaCar.setId(i);
			servicesCall.add(resultBlaBlaCar);

			// CITYBIKES
			ExperimentResult resultCityBikes = new ExperimentResult();
			resultCityBikes = callCityBikes();
			resultCityBikes.setId(i);
			servicesCall.add(resultCityBikes);

		}
		System.out.println("TEST SERVICES ENDED");
		Utilities.genericWriteFileNew(servicesCall, "servicesTest.csv");

	}

	private static ExperimentResult callCityBikes() {
		ExperimentResult result = new ExperimentResult();

		// create random inputs
		String city = Utilities.randomCityBikes();

		double responseTime = 0.0;
		long startTime = System.currentTimeMillis();

		// call Google Transit
		CityBikesAPIWrapper citybikeWrapper = new CityBikesAPIWrapper();
		ArrayList<CityBikesStation> stations = new ArrayList<CityBikesStation>();
		stations = citybikeWrapper.getStations(city);

		long stopTime = System.currentTimeMillis();
		responseTime = stopTime - startTime;

		// memorize parameters to result variable
		result.setDv1("CityBikes");
		result.setDv2(city);
		result.setDv3(city);
		result.setDv4(responseTime);

		// number of alternatives
		result.setDv5(stations.size());
		result.setDv6(0);
		return result;
	}

	private static ExperimentResult callGoogleTransit() {
		ExperimentResult result = new ExperimentResult();

		// create random inputs
		String source = Utilities.randomCityGlobal();

		String destination = "";

		boolean notFound = true;
		while (notFound) {
			destination = Utilities.randomCityGlobal();
			if (!destination.equalsIgnoreCase(source)) {
				notFound = false;

			}

		}

		double responseTime = 0.0;
		long startTime = System.currentTimeMillis();

		// call Google Transit
		GoogleAPIWrapper googleWrapper = new GoogleAPIWrapper();
		ArrayList<GoogleTransitAlternative> googleAlternatives = new ArrayList<GoogleTransitAlternative>();
		googleAlternatives = googleWrapper.callTransit(source, destination);

		long stopTime = System.currentTimeMillis();
		responseTime = stopTime - startTime;

		// memorize parameters to result variable
		result.setDv1("GoogleTransit");
		result.setDv2(source);
		result.setDv3(destination);
		result.setDv4(responseTime);

		// number of alternatives
		result.setDv5(googleAlternatives.size());

		// number of changes (average)
		if (googleAlternatives.size() == 0) {
			result.setDv6(0);
		} else {
			float averageChanges = getAverageGoogleTransit(googleAlternatives);
			result.setDv6(averageChanges);
		}

		return result;
	}

	private static float getAverageGoogleTransit(
			ArrayList<GoogleTransitAlternative> alternatives) {
		float average = 0;
		float total = 0;
		for (int i = 0; i < alternatives.size(); i++) {
			GoogleTransitAlternative current = alternatives.get(i);
			float legs = current.getNumber_changes();
			total = total + legs;
		}
		average = total / alternatives.size();
		return average;
	}

	private static ExperimentResult callLondonJP() {
		ExperimentResult result = new ExperimentResult();
		double responseTime = 0.0;
		long startTime = System.currentTimeMillis();

		String from = Utilities.randomLondonJPCode();
		String to = "";

		boolean notFound = true;
		while (notFound) {
			to = Utilities.randomLondonJPCode();
			if (!to.equalsIgnoreCase(from)) {
				notFound = false;

			}

		}

		// call London Journey Planner
		LondonJPAPIWrapper londonWrapper = new LondonJPAPIWrapper();
		ArrayList<TripAlternativeLondonJP> londonAlternatives = new ArrayList<TripAlternativeLondonJP>();
		londonAlternatives = londonWrapper.getLondonJPAlternatives(from, to);

		long stopTime = System.currentTimeMillis();
		responseTime = stopTime - startTime;

		// memorize parameters to result variable
		result.setDv1("LondonJP");
		result.setDv2("london");
		result.setDv3("manchester");
		result.setDv4(responseTime);

		// number of alternatives
		result.setDv5(londonAlternatives.size());

		// number of changes (average)
		float averageChanges = getAverageLondonJP(londonAlternatives);
		result.setDv6(averageChanges);

		return result;
	}

	private static float getAverageLondonJP(
			ArrayList<TripAlternativeLondonJP> alternatives) {
		float average = 0;
		float total = 0;
		for (int i = 0; i < alternatives.size(); i++) {
			TripAlternativeLondonJP current = alternatives.get(i);
			float legs = current.getNumber_changes();
			total = total + legs;
		}
		average = total / alternatives.size();
		return average;
	}

	private static ExperimentResult callBlaBlaCar() {

		ExperimentResult result = new ExperimentResult();
		// create random inputs
		String source = Utilities.randomCityItaly();

		String destination = "";

		boolean notFound = true;
		while (notFound) {
			destination = Utilities.randomCityItaly();
			if (!destination.equalsIgnoreCase(source)) {
				notFound = false;

			}

		}

		double responseTime = 0.0;
		long startTime = System.currentTimeMillis();

		// call BlaBlaCar
		BlaBlaCarAPIWrapper blablacarWrapper = new BlaBlaCarAPIWrapper();
		ArrayList<TripAlternativeBlaBlaCar> blaBlaCarAlternatives = new ArrayList<TripAlternativeBlaBlaCar>();
		blaBlaCarAlternatives = blablacarWrapper.getBlaBlaCarAlternatives(
				source, destination, null);

		long stopTime = System.currentTimeMillis();
		responseTime = stopTime - startTime;

		// memorize parameters to result variable
		result.setDv1("BlaBlaCar");
		result.setDv2(source);
		result.setDv3(destination);
		result.setDv4(responseTime);

		// number of alternatives
		result.setDv5(blaBlaCarAlternatives.size());

		// number of changes (average) - fixed to 1
		result.setDv6(1);

		return result;
	}

	private static ExperimentResult callViaggiaTrento() {

		ExperimentResult result = new ExperimentResult();
		ViaggiaTrentoAPIWrapper viaggiaWrapper = new ViaggiaTrentoAPIWrapper();
		double responseTime = 0.0;
		long startTime = System.currentTimeMillis();

		// generate From and To randomly

		// call ViaggiaTrento
		JSONArray VTResult = new JSONArray();
		// coordinated derivation from addresses
		GoogleAPIWrapper googleWrapper = new GoogleAPIWrapper();

		// create random inputs
		String source = Utilities.randomCityTrento();

		String destination = "";

		boolean notFound = true;
		while (notFound) {
			destination = Utilities.randomCityTrento();
			if (!destination.equalsIgnoreCase(source)) {
				notFound = false;

			}

		}

		String coordinatesFrom = googleWrapper.getCoordinates(source);
		String coordinatesTo = googleWrapper.getCoordinates(destination);
		result.setDv2(source);
		result.setDv3(destination);

		VTResult = vtCall(coordinatesFrom, coordinatesTo);

		long stopTime = System.currentTimeMillis();
		responseTime = stopTime - startTime;

		// memorize parameters to result variable
		result.setDv1("ViaggiaTrento");
		result.setDv4(responseTime);

		// number of alternatives
		result.setDv5(VTResult.length());

		// number of changes (average)
		if (VTResult.length() == 0) {
			result.setDv6(0);
		} else {
			ArrayList<TravelViaggiaTrento> alternatives = retrieveVTAlternatives(VTResult);
			float averageChanges = getAverageViaggiaTrento(alternatives);
			result.setDv6(averageChanges);
		}

		return result;
	}

	private static float getAverageViaggiaTrento(
			ArrayList<TravelViaggiaTrento> alternatives) {
		float average = 0;
		float total = 0;
		for (int i = 0; i < alternatives.size(); i++) {
			TravelViaggiaTrento current = alternatives.get(i);
			int steps = current.getSteps().size();
			total = total + steps;
		}
		average = total / alternatives.size();
		return average;
	}

	private static JSONArray vtCall(String from, String to) {

		JSONArray result = new JSONArray();

		result = CallViaggiaTrento(from, to);

		return result;
	}

	private static ArrayList<TravelViaggiaTrento> retrieveVTAlternatives(
			JSONArray jsonArr) {
		ArrayList<TravelViaggiaTrento> alternatives = new ArrayList<TravelViaggiaTrento>();

		for (int i = 0; i < jsonArr.length(); i++) {

			JSONObject numbers = (JSONObject) jsonArr.get(i);
			ArrayList<String> routes = new ArrayList<String>();
			ArrayList<String> steps = new ArrayList<String>();
			ArrayList<String> routeId = new ArrayList<String>();

			JSONArray leg = new JSONArray();
			leg = numbers.getJSONArray("leg");
			String complete = "";

			// System.out.println("\n" + leg.length() + "\n");

			for (int j = 0; j < leg.length(); j++) {
				JSONObject legs = (JSONObject) leg.get(j);

				JSONObject transport = (JSONObject) legs
						.getJSONObject("transport");
				String type = transport.getString("type").toUpperCase();

				JSONObject to = (JSONObject) legs.getJSONObject("to");
				String nameTO = to.getString("name");

				JSONObject from = (JSONObject) legs.getJSONObject("from");
				String nameFrom = from.getString("name");

				if (type.equals("BUS")) {
					routeId.add(transport.getString("routeId"));
					String busNumber = transport.getString("routeShortName");
					complete = "*" + busNumber + "*\n" + "          *FROM* "
							+ nameFrom + "\n" + "          *TO* " + nameTO;
				} else {
					routeId.add("999");
					complete = "\n" + "          *FROM* " + nameFrom + "\n"
							+ "          *TO* " + nameTO;
				}

				routes.add(complete);
				steps.add(type);
			}

			int minutes = (int) TimeUnit.MILLISECONDS.toMinutes(numbers
					.getInt("duration"));
			alternatives.add(new TravelViaggiaTrento("" + minutes, steps,
					routes, routeId));
		}
		return alternatives;

	}

	private static JSONArray CallViaggiaTrento(String from, String to) {
		ViaggiaTrentoAPIWrapper viaggiaWrapper = new ViaggiaTrentoAPIWrapper();

		JSONArray alternatives = new JSONArray();

		alternatives = viaggiaWrapper.getViaggiaTrentoResponse(from, to, null,
				"TRANSIT", "fastest");

		return alternatives;
	}

	private static ExperimentResult callRome2Rio() {

		ExperimentResult result = new ExperimentResult();

		// create random inputs
		String source = Utilities.randomCityGlobal();

		String destination = "";

		boolean notFound = true;
		while (notFound) {
			destination = Utilities.randomCityGlobal();
			if (!destination.equalsIgnoreCase(source)) {
				notFound = false;

			}

		}

		long responseTime = 0;
		long startTime = System.currentTimeMillis();
		Rome2RioAPIWrapper rome2RioWrapper = new Rome2RioAPIWrapper();

		// call Rome2Rio Service
		JSONObject rome2RioResult = new JSONObject();
		rome2RioResult = rome2RioCall(source, destination);

		// extract the number of alternatives

		result.setDv2(source);
		result.setDv3(destination);

		// memorize parameters to result variable
		result.setDv1("Rome2Rio");

		// memorize the responseTime
		long stopTime = System.currentTimeMillis();
		responseTime = stopTime - startTime;
		// String planGenerationTime = elapsed(responseTime);
		// System.out.println("Initial Plan Generation Time = "
		// + planGenerationTime);
		result.setDv4(responseTime);

		// number of alternatives
		ArrayList<TripAlternativeRome2Rio> alternatives = new ArrayList<TripAlternativeRome2Rio>();
		alternatives = rome2RioWrapper.retrieveAlternatives(rome2RioResult);
		result.setDv5(alternatives.size());

		float averageChanges = getAverageRome2Rio(alternatives);
		result.setDv6(averageChanges);

		return result;
	}

	private static float getAverageRome2Rio(
			ArrayList<TripAlternativeRome2Rio> alternatives) {
		float average = 0;
		int total = 0;
		for (int i = 0; i < alternatives.size(); i++) {
			TripAlternativeRome2Rio current = alternatives.get(i);
			int changes = current.getNumber_changes();
			total = total + changes;
		}
		average = total / alternatives.size();
		return average;
	}

	private static JSONObject rome2RioCall(String from, String to) {
		Rome2RioAPIWrapper rome2RioWrapper = new Rome2RioAPIWrapper();

		JSONObject result = new JSONObject();

		result = rome2RioWrapper.getRome2RioResponse(from, to);

		return result;
	}

	public static String elapsed(long duration) {
		final TimeUnit scale = MILLISECONDS;

		long days = scale.toDays(duration);
		duration -= DAYS.toMillis(days);
		long hours = scale.toHours(duration);
		duration -= HOURS.toMillis(hours);
		long minutes = scale.toMinutes(duration);
		duration -= MINUTES.toMillis(minutes);
		long seconds = scale.toSeconds(duration);
		duration -= SECONDS.toMillis(seconds);
		long millis = scale.toMillis(duration);
		duration -= MILLISECONDS.toMillis(seconds);
		long nanos = scale.toNanos(duration);

		return String
				.format("%d days, %d hours, %d minutes, %d seconds, %d millis, %d nanos",
						days, hours, minutes, seconds, millis, nanos);
	}

}
