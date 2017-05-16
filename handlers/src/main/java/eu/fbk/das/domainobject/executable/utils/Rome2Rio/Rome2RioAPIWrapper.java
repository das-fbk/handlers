package eu.fbk.das.domainobject.executable.utils.Rome2Rio;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class Rome2RioAPIWrapper {

	public ArrayList<TripAlternativeRome2Rio> getRome2RioAlternatives(
			String partenza, String destinazione) {

		ArrayList<TripAlternativeRome2Rio> alternatives = new ArrayList<TripAlternativeRome2Rio>();
		String result = callURL("http://free.rome2rio.com/api/1.4/json/Search?key=Yt1V3vTI&oName="
				+ partenza + "&dName=" + destinazione);
		if (result.equalsIgnoreCase("erroreAPI")) {
			return alternatives;
		} else {
			// System.out.println(result);
			JSONObject jsonObj = new JSONObject(result);
			JSONArray routes = new JSONArray();
			routes = jsonObj.getJSONArray("routes");
			// System.out.println("routes.length(): "+routes.length());
			for (int i = 0; i < routes.length(); i++) {

				System.out.println("Soluzione " + i + ":");
				JSONObject route = (JSONObject) routes.get(i);
				System.out.println(route);

				// transportation mean
				String mean = route.getString("name");

				// changes
				JSONArray changes = new JSONArray();
				changes = route.getJSONArray("segments");

				int number_changes = -1;
				for (int j = 0; j < changes.length(); j++) {
					number_changes++;
				}

				// PRICES
				JSONArray prices = new JSONArray();
				prices = route.getJSONArray("indicativePrices");
				JSONObject price = (JSONObject) prices.get(0);

				// price
				Double priceInd = price.getDouble("price");

				// distance
				Double distance = route.getDouble("distance");

				// duration
				Double duration = route.getDouble("totalDuration");

				// places
				JSONArray places = new JSONArray();
				places = jsonObj.getJSONArray("places");

				// vehicles
				JSONArray vehicles = new JSONArray();
				vehicles = jsonObj.getJSONArray("vehicles");

				// agencies
				JSONArray agencies = new JSONArray();
				agencies = jsonObj.getJSONArray("agencies");

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
					String vehicleValue = retrieveVehicle(segmentVehicle,
							vehicles);
					// search Agency
					String agencyValue = retrieveAgency(agencyNumber, agencies);

					// create and add the segment to the list of segments
					Segment newSegment = new Segment(vehicleValue, agencyValue,
							fromValue, toValue);
					segments.add(newSegment);
				}

				TripAlternativeRome2Rio alternative = new TripAlternativeRome2Rio(
						i, mean, priceInd, duration, distance, segments,
						number_changes);
				alternatives.add(alternative);

			}
		}
		return alternatives;
	}

	public JSONObject getRome2RioResponse(String partenza, String destinazione) {

		JSONObject response = null;
		String result = callURL("http://free.rome2rio.com/api/1.4/json/Search?key=Yt1V3vTI&oName="
				+ partenza + "&dName=" + destinazione);
		if (result.equalsIgnoreCase("erroreAPI")) {
			return response;
		} else {
			response = new JSONObject(result);
		}
		return response;
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
