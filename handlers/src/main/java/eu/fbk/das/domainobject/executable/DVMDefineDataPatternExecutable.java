package eu.fbk.das.domainobject.executable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.w3c.dom.Element;

import eu.fbk.das.domainobject.executable.utils.Segment;
import eu.fbk.das.domainobject.executable.utils.TripAlternative;
import eu.fbk.das.domainobject.executable.utils.BotTelegram.TravelAssistantBot;
import eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging.Menu;
import eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging.Travel;
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
	private ArrayList<TripAlternative> alternatives;
	private TravelAssistantBot bot;
	private JSONObject rome2rioJson;

	// bot elements
	public static final String PRICE = "\u2193" + "\ud83d\udcb5";
	public static final String TIME = "\u2193" + "\u23f3";
	public static final String CHANGES = "\u2193" + "\u0058";
	public static final String DISTANCE = "\u2193" + "\u33ce";

	private static ArrayList<Travel> travels;
	// end bot elements

	private static int hoaaCounter = 1;

	public DVMDefineDataPatternExecutable(ProcessEngine processEngine,
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

			// read plannerOutput value
			Element planOut = doi
					.getStateVariableContentByName("PlannerOutput");
			String planOutValue = planOut.getFirstChild().getNodeValue();

			/****************** AD HOC - ANDRA ELIMINATO ************************************/
			if (planOutValue.equals("")) {
				List<DomainObjectInstance> dois = pe.getDomainObjectInstances();
				for (DomainObjectInstance current : dois) {
					if (current.getId().equals("User_1")) {
						Element elemResultList = current
								.getStateVariableContentByName("TA_IdentifyLeg-1.ResultList");
						Element el = current
								.getStateVariableContentByName("PlannerOutput");
						el.setTextContent(elemResultList.getFirstChild()
								.getNodeValue());
						planOutValue = elemResultList.getFirstChild()
								.getNodeValue();
					}
				}
			}
			/******************************************************************************/

			// extract the current mobility Service (i.e., Rome2Rio,BlaBlaCar,
			// etc..
			String[] parts = planOutValue.split("<>");
			String service = (String) parts[0];
			String PlannerOutputValue = parts[1];
			if (service.equalsIgnoreCase("Rome2Rio")) {
				JSONObject jsonObj = new JSONObject(PlannerOutputValue);

				String planList = this.extractPlanListOfRome2Rio(jsonObj);

				// update the PlanList variable value
				Element planElement = doi
						.getStateVariableContentByName("PlanList");
				planElement.setTextContent(planList);
				// save result in response variable
				doi.setStateVariableContentByVarName("PlanList", planElement);

				// define the viewer pattern for the bot visualization
				// set the ReplyKeyboardMarkup to visualize in showResults

				// create the DataPattern for Rome2Rio
				// ArrayList<TripAlternative> alternatives =
				// extractRome2RioAlternatives(jsonObj);

				// memorize the DataPattern in to the variable
				Element dataPatternElement = doi
						.getStateVariableContentByName("DataPattern");
				dataPatternElement.setTextContent("Rome2Rio");
				// save result in response variable
				doi.setStateVariableContentByVarName("DataPattern",
						dataPatternElement);
			} else if (service.equalsIgnoreCase("BlaBlaCar")) {
				// update the PlanList variable value
				Element planElement = doi
						.getStateVariableContentByName("PlanList");
				planElement.setTextContent(PlannerOutputValue);
				// save result in response variable
				doi.setStateVariableContentByVarName("PlanList", planElement);

				// memorize the DataPattern in to the variable
				Element dataPatternElement = doi
						.getStateVariableContentByName("DataPattern");
				dataPatternElement.setTextContent("BlaBlaCar");
				// save result in response variable
				doi.setStateVariableContentByVarName("DataPattern",
						dataPatternElement);

			}

			// set activity to executed
			currentConcrete.setExecuted(true);
			return;
		}
		logger.debug("Domain Object without a state! ");
		currentConcrete.setExecuted(true);
		return;
	}

	private ArrayList<TripAlternative> extractRome2RioAlternatives(
			JSONObject plannerOutputValue) {

		ArrayList<TripAlternative> alternatives = new ArrayList<TripAlternative>();

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
			Double priceInd = null;
			prices = route.getJSONArray("indicativePrices");
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
			int from = (int) route.getLong("depPlace");

			JSONObject placeFrom = places.getJSONObject(from);
			double fromLat = placeFrom.getDouble("lat");
			double fromLng = placeFrom.getDouble("lng");

			// to
			int to = (int) route.getLong("arrPlace");
			JSONObject placeTo = places.getJSONObject(to);
			double toLat = placeTo.getDouble("lat");
			double toLng = placeTo.getDouble("lng");

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
				System.out.println("SEGMENTL " + segment);
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
			TripAlternative alternative = new TripAlternative(mean, priceInd,
					distance, duration, segments, number_changes);
			alternatives.add(alternative);

			// save alternatives to be used later by the bot
			bot.setAlternatives(alternatives);

		}

		return alternatives;
	}

	private String extractPlanListOfRome2Rio(JSONObject plannerOutputValue) {

		ArrayList<TripAlternative> alternatives = new ArrayList<TripAlternative>();
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
			Double priceInd = null;
			prices = route.getJSONArray("indicativePrices");
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
			int from = (int) route.getLong("depPlace");

			JSONObject placeFrom = places.getJSONObject(from);
			double fromLat = placeFrom.getDouble("lat");
			double fromLng = placeFrom.getDouble("lng");

			// to
			int to = (int) route.getLong("arrPlace");
			JSONObject placeTo = places.getJSONObject(to);
			double toLat = placeTo.getDouble("lat");
			double toLng = placeTo.getDouble("lng");

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
				System.out.println("SEGMENTL " + segment);
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
			TripAlternative alternative = new TripAlternative(mean, priceInd,
					distance, duration, segments, number_changes);
			alternatives.add(alternative);

			// save alternatives to be used later by the bot
			bot.setAlternatives(alternatives);

		}

		planListResult = extractString(alternatives);
		return planListResult;
	}

	private String extractString(ArrayList<TripAlternative> alternatives) {
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

	// elements for Rome2Rio results visualization in the bot telegram

	private static ReplyKeyboardMarkup keyboardZoneRome2RioResult(Long ChatID,
			ArrayList<TripAlternative> alternatives, Menu menu, String filter) {

		ReplyKeyboardMarkup replyKeyboardMarkup = keyboard();
		List<KeyboardRow> keyboard = new ArrayList<>();

		keyboard.add(new KeyboardRow());
		keyboard.get(0).add(PRICE);
		keyboard.get(0).add(TIME);
		keyboard.get(0).add(DISTANCE);
		keyboard.get(0).add(CHANGES);

		travels = new ArrayList<Travel>();

		for (int i = 0; i < alternatives.size(); i++) {
			String mean = alternatives.get(i).getMean();
			Double duration = alternatives.get(i).getDuration();
			Double cost = alternatives.get(i).getPrice();
			Double distance = alternatives.get(i).getDistance();
			Integer numberChanges = alternatives.get(i).getNumber_changes();

			String durationString = "";
			if (duration < 60) {
				if (duration == 1) {
					durationString = duration.toString() + " min";
				} else {
					durationString = duration.toString() + " mins";
				}
			} else {
				int rest = duration.intValue() % 60;
				int hour = duration.intValue() / 60;
				durationString = hour + "." + rest + " h";
			}

			String distanceString = distance.toString() + " Km";
			String costString = cost.toString() + " \u20ac";
			String shangesString = numberChanges.toString();
			if (numberChanges == 1) {
				shangesString += " change";
			} else {
				shangesString += " changes";
			}

			mean = setKeyboardJourneyOption(i, mean);

			travels.add(new Travel(i, mean, durationString, costString,
					distanceString, shangesString));

		}

		switch (filter) {
		case TIME:
			Collections.sort(travels, Travel.timeComparator);
			break;
		case DISTANCE:
			Collections.sort(travels, Travel.distanceComparator);
			break;
		case CHANGES:
			Collections.sort(travels, Travel.changesComparator);
			break;
		default:
			Collections.sort(travels, Travel.priceComparator);
			break;
		}

		for (int i = 0; i < travels.size(); i++) {
			keyboard.add(keyboardRowButton(travels.get(i).getMean()));
		}

		replyKeyboardMarkup.setKeyboard(keyboard);

		// Current.setMenu(chatId, menu);
		return replyKeyboardMarkup;
	}

	private static ReplyKeyboardMarkup keyboard() {
		ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
		replyKeyboardMarkup.setSelective(true);
		replyKeyboardMarkup.setResizeKeyboard(true);
		replyKeyboardMarkup.setOneTimeKeyboad(false);

		return replyKeyboardMarkup;
	}

	private static KeyboardRow keyboardRowButton(String value) {
		KeyboardRow keyboardRow = new KeyboardRow();
		keyboardRow.add(value);

		return keyboardRow;
	}

	public static ReplyKeyboardMarkup keyboardRome2RioResult(long chatId,
			ArrayList<TripAlternative> alternatives, String filter) {
		return keyboardZoneRome2RioResult(chatId, alternatives,
				Menu.ROME2RIORESULT, filter);
	}

	public static String setKeyboardJourneyOption(int index, String mean) {
		String indexString = Integer.toString(index);
		mean = Pattern.compile("Night train", Pattern.LITERAL).matcher(mean)
				.replaceAll(Matcher.quoteReplacement("Night_trai"));

		mean = Pattern.compile("night train", Pattern.LITERAL).matcher(mean)
				.replaceAll(Matcher.quoteReplacement("night_trai"));

		mean = Pattern.compile("Night bus", Pattern.LITERAL).matcher(mean)
				.replaceAll(Matcher.quoteReplacement("Night_bu"));

		mean = Pattern.compile("night bus", Pattern.LITERAL).matcher(mean)
				.replaceAll(Matcher.quoteReplacement("night_bu"));

		mean = Pattern.compile("Car ferry", Pattern.LITERAL).matcher(mean)
				.replaceAll(Matcher.quoteReplacement("Car_ferr"));

		mean = Pattern.compile("car ferry", Pattern.LITERAL).matcher(mean)
				.replaceAll(Matcher.quoteReplacement("car_ferr"));

		if (mean.contains("Line")) {
			mean = Pattern
					.compile(mean.substring(0, mean.indexOf("train")),
							Pattern.LITERAL).matcher(mean)
					.replaceAll(Matcher.quoteReplacement("Train"));
		}

		if (mean.contains("line")) {
			mean = Pattern
					.compile(mean.substring(0, mean.indexOf("train")),
							Pattern.LITERAL).matcher(mean)
					.replaceAll(Matcher.quoteReplacement("train"));
		}

		mean = Pattern.compile(", train to", Pattern.LITERAL).matcher(mean)
				.replaceAll(Matcher.quoteReplacement(" to"));

		mean = Pattern
				.compile("Train", Pattern.LITERAL)
				.matcher(mean)
				.replaceFirst(
						Matcher.quoteReplacement(indexString + " "
								+ "\uD83D\uDE84" + "Train"));

		mean = Pattern
				.compile("train", Pattern.LITERAL)
				.matcher(mean)
				.replaceAll(
						Matcher.quoteReplacement(indexString + " "
								+ "\uD83D\uDE84" + "train"));

		mean = Pattern
				.compile("Night_trai", Pattern.LITERAL)
				.matcher(mean)
				.replaceFirst(
						Matcher.quoteReplacement(indexString + " "
								+ "\uD83D\uDE86" + "Night train"));

		mean = Pattern
				.compile("night_trai", Pattern.LITERAL)
				.matcher(mean)
				.replaceAll(
						Matcher.quoteReplacement(indexString + " "
								+ "\uD83D\uDE86" + "night train"));

		mean = Pattern
				.compile("Bus", Pattern.LITERAL)
				.matcher(mean)
				.replaceFirst(
						Matcher.quoteReplacement(indexString + " "
								+ "\uD83D\uDE8C" + "Bus"));

		mean = Pattern
				.compile("bus", Pattern.LITERAL)
				.matcher(mean)
				.replaceAll(
						Matcher.quoteReplacement(indexString + " "
								+ "\uD83D\uDE8C" + "bus"));

		mean = Pattern
				.compile("Night_bu", Pattern.LITERAL)
				.matcher(mean)
				.replaceFirst(
						Matcher.quoteReplacement(indexString + " "
								+ "\ud83d\ude8d" + "Night bus"));

		mean = Pattern
				.compile("night_bu", Pattern.LITERAL)
				.matcher(mean)
				.replaceAll(
						Matcher.quoteReplacement(indexString + " "
								+ "\ud83d\ude8d" + "night bus"));

		mean = Pattern
				.compile("Drive", Pattern.LITERAL)
				.matcher(mean)
				.replaceFirst(
						Matcher.quoteReplacement(indexString + " "
								+ "\uD83D\uDE98" + "Drive"));

		mean = Pattern
				.compile("drive", Pattern.LITERAL)
				.matcher(mean)
				.replaceAll(
						Matcher.quoteReplacement(indexString + " "
								+ "\uD83D\uDE98" + "drive"));

		mean = Pattern
				.compile("Car_ferr", Pattern.LITERAL)
				.matcher(mean)
				.replaceFirst(
						Matcher.quoteReplacement(indexString + " " + "\u26F4"
								+ "Car ferry"));

		mean = Pattern
				.compile("car_ferr", Pattern.LITERAL)
				.matcher(mean)
				.replaceAll(
						Matcher.quoteReplacement(indexString + " " + "\u26F4"
								+ "car ferry"));

		mean = Pattern
				.compile("Fly", Pattern.LITERAL)
				.matcher(mean)
				.replaceFirst(
						Matcher.quoteReplacement(indexString + " "
								+ "\uD83D\uDEEB" + "Fly"));

		mean = Pattern
				.compile("fly", Pattern.LITERAL)
				.matcher(mean)
				.replaceAll(
						Matcher.quoteReplacement(indexString + " "
								+ "\uD83D\uDEEB" + "fly"));

		mean = Pattern
				.compile("Shuttle", Pattern.LITERAL)
				.matcher(mean)
				.replaceFirst(
						Matcher.quoteReplacement(indexString + " "
								+ "\uD83D\uDE90" + "Shuttle"));

		mean = Pattern
				.compile("shuttle", Pattern.LITERAL)
				.matcher(mean)
				.replaceAll(
						Matcher.quoteReplacement(indexString + " "
								+ "\uD83D\uDE90" + "shuttle"));

		mean = Pattern
				.compile("Rideshare", Pattern.LITERAL)
				.matcher(mean)
				.replaceFirst(
						Matcher.quoteReplacement(indexString + " "
								+ "\uD83C\uDFCE" + "Rideshare"));

		mean = Pattern
				.compile("rideshare", Pattern.LITERAL)
				.matcher(mean)
				.replaceAll(
						Matcher.quoteReplacement(indexString + " "
								+ "\uD83C\uDFCE" + "rideshare"));

		mean = Pattern
				.compile("Eurotunnel", Pattern.LITERAL)
				.matcher(mean)
				.replaceFirst(
						Matcher.quoteReplacement(indexString + " "
								+ "\uD83D\uDD73" + "Eurotunnel"));

		mean = Pattern
				.compile("eurotunnel", Pattern.LITERAL)
				.matcher(mean)
				.replaceAll(
						Matcher.quoteReplacement(indexString + " "
								+ "\uD83D\uDD73" + "eurotunnel"));

		mean = Pattern
				.compile("Taxi", Pattern.LITERAL)
				.matcher(mean)
				.replaceFirst(
						Matcher.quoteReplacement(indexString + " "
								+ "\ud83d\ude95" + "Taxi"));

		mean = Pattern
				.compile("taxi", Pattern.LITERAL)
				.matcher(mean)
				.replaceAll(
						Matcher.quoteReplacement(indexString + " "
								+ "\ud83d\ude95" + "taxi"));

		return mean;
	}

	// end Rome2Rio Elements

}
