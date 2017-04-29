package eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by antbucc
 */
public class Database {

	// TODO List of errors in Autobus List:
	// TODO - A, B, C : always return a empty TimeTable
	// TODO - 1, 14R : List<Stops>.size() != List<Times>.size()
	// TODO - FuR : doesn't appear in List<Route>

	// region final

	private static final String AGENCY_AUTOBUS = "12";
	private static final String AGENCY_TRAINS_BV = "6";
	private static final String AGENCY_TRAINS_TB = "5";
	private static final String AGENCY_TRAINS_TM = "10";
	private static final String AGENCY_PARKINGS = "COMUNE_DI_TRENTO";
	private static final String AGENCY_BIKESHARINGS = "BIKE_SHARING_TOBIKE_TRENTO";
	private static final String SERVER_URL = "https://tn.smartcommunitylab.it/core.mobility";

	// endregion final

	// private static MobilityDataService dataService = new MobilityDataService(
	// SERVER_URL);
	//
	// static {

	// }

	// region utilities

	private static String capitalize(String s) {
		String text = "";
		String[] words = s.split("\\s");
		for (String string : words)
			text += (string.substring(0, 1).toUpperCase()
					+ string.substring(1).toLowerCase() + " ");
		return text.substring(0, text.length() - 1);
	}

	private static String addSpace(String s) {
		return s.replace("/", " / ").trim();
	}

	// endregion utilities

	// region gets

	public static List<String> getRome2RioSource() throws ExecutionException {
		List<String> sources = new ArrayList<String>();
		sources.add("TRENTO");
		sources.add("BOLZANO");
		sources.add("LONDRA");
		return sources;
	}

	public static List<String> getRome2RioDestination()
			throws ExecutionException {
		List<String> sources = new ArrayList<String>();
		sources.add("TRENTO");
		sources.add("BOLZANO");
		sources.add("LONDRA");
		return sources;
	}

	// endregion gets

}
