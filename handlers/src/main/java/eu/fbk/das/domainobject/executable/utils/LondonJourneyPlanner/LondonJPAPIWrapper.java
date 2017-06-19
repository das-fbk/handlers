package eu.fbk.das.domainobject.executable.utils.LondonJourneyPlanner;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class LondonJPAPIWrapper {

	public ArrayList<TripAlternativeLondonJP> getLondonJPAlternatives(
			String from, String to) {

		ArrayList<TripAlternativeLondonJP> alternatives = new ArrayList<TripAlternativeLondonJP>();
		// https://api.tfl.gov.uk/Journey/JourneyResults/AB16 9UG/to/LS23
		// 6LG?nationalSearch=true&app_id=d654aa64&app_key=c4fb4a4d483410a17439e56793b1101e
		String result = callURL("https://api.tfl.gov.uk/journey/journeyresults/"
				+ from + "/to/" + to);
		if (result.equalsIgnoreCase("erroreAPI")) {
			return alternatives;
		} else {
			// System.out.println(result);
			JSONObject jsonObj = new JSONObject(result);
			JSONArray journeys = new JSONArray();
			journeys = jsonObj.getJSONArray("journeys");
			// System.out.println("routes.length(): "+routes.length());
			for (int i = 0; i < journeys.length(); i++) {
				TripAlternativeLondonJP alternative = new TripAlternativeLondonJP();
				// System.out.println("Soluzione " + i + ":");
				JSONObject journey = (JSONObject) journeys.get(i);
				// System.out.println(journey);

				// changes
				JSONArray changes = new JSONArray();
				changes = journey.getJSONArray("legs");

				alternative.setNumber_changes(changes.length());
				alternatives.add(alternative);

			}
		}
		return alternatives;
	}

	// returns the result of the API call as string
	public static String callURL(String myURL) {
		// System.out.println(myURL);
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
