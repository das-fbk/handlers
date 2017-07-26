package eu.fbk.das.domainobject.executable.utils.Rome2Rio;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.json.JSONArray;
import org.json.JSONObject;

import eu.fbk.das.domainobject.executable.utils.GoogleAPI.GoogleAPIWrapper;

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

				// System.out.println("Soluzione " + i + ":");
				JSONObject route = (JSONObject) routes.get(i);
				// System.out.println(route);

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
				Double priceInd = 0.0;
				JSONArray prices = new JSONArray();
				if (route.has("indicativePrices")) {
					prices = route.getJSONArray("indicativePrices");
				}

				if (prices != null && prices.length() != 0) {
					JSONObject price = (JSONObject) prices.get(0);
					// price
					priceInd = price.getDouble("price");
				}

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
					// System.out.println("SEGMENTL " + segment);
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

	public ArrayList<TravelsRomeToRioAfterChoose> getRome2RioAfterChoose(
			String all, String partenza, String destinazione) {

		ArrayList<TravelsRomeToRioAfterChoose> alternatives = new ArrayList<TravelsRomeToRioAfterChoose>();

		// CALL r2r API with coordinates
		GoogleAPIWrapper googleWrapper = new GoogleAPIWrapper();
		String originCoord = googleWrapper.getCoordinates(partenza);
		String destCoord = googleWrapper.getCoordinates(destinazione);
		String url = "http://free.rome2rio.com/api/1.4/json/Search?key=Yt1V3vTI&oPos="
				+ originCoord
				+ "&oKind=addressd&dPos="
				+ destCoord
				+ "&dKind=address";

		String result = callURL(url);

		if (result.equalsIgnoreCase("erroreAPI")) {
			return alternatives;
		} else {
			JSONObject jsonObj = new JSONObject(result);
			JSONArray routes = new JSONArray();

			routes = jsonObj.getJSONArray("routes");

			for (int i = 0; i < routes.length(); i++) {

				StringTokenizer stk = new StringTokenizer(all, "*");
				ArrayList<String> help = new ArrayList<String>();

				while (stk.hasMoreTokens()) {
					String token = stk.nextToken();
					StringTokenizer stk1 = new StringTokenizer(token, ";");

					while (stk1.hasMoreTokens()) {
						String token1 = stk1.nextToken();
						help.add(token1);
					}

				}

				JSONArray segments = new JSONArray();
				segments = ((JSONObject) routes.get(i))
						.getJSONArray("segments");
				for (int y = 0; y < segments.length(); y++) {
					JSONObject seg = (JSONObject) segments.get(y);

					Integer vei = seg.getInt("vehicle");
					Integer dep = seg.getInt("depPlace");
					Integer arr = seg.getInt("arrPlace");

					JSONArray vehicles = new JSONArray();
					vehicles = jsonObj.getJSONArray("vehicles");
					String vehicle1 = ((JSONObject) vehicles.get(vei))
							.getString("name");

					JSONArray places = new JSONArray();
					places = jsonObj.getJSONArray("places");
					String depPlace1 = ((JSONObject) places.get(dep))
							.getString("shortName");
					String arrPlace1 = ((JSONObject) places.get(arr))
							.getString("shortName");

					String agency1 = "";
					JSONArray agencies = new JSONArray();
					if (seg.has("agencies")) {
						agencies = seg.getJSONArray("agencies");
						Integer agg = ((JSONObject) agencies.get(0))
								.getInt("agency");
						JSONArray agge = new JSONArray();
						agge = jsonObj.getJSONArray("agencies");
						agency1 = ((JSONObject) agge.get(agg))
								.getString("name");
					} else {
						agency1 = "999";
					}

					for (int t = 0; t < help.size() - 3; t++) {
						Integer duration = seg.getInt("transitDuration");
						Double distance = seg.getDouble("distance");
						Integer priceInd = 0;
						if (seg.has("indicativePrices")) {
							JSONArray prices = seg
									.getJSONArray("indicativePrices");
							JSONObject price = (JSONObject) prices.get(0);
							priceInd = price.getInt("price");
						} else {
							priceInd = -1;
						}

						TravelsRomeToRioAfterChoose alternative = new TravelsRomeToRioAfterChoose(
								depPlace1, arrPlace1, duration,
								distance.intValue(), priceInd, vehicle1,
								agency1, ordinal(y + 1));
						if (help.get(t).equals(vehicle1)
								&& help.get(t + 1).equals(agency1)
								&& help.get(t + 2).equals(depPlace1)
								&& help.get(t + 3).equals(arrPlace1)) {
							alternatives.add(alternative);
						}

					}

				}
				if (alternatives.size() != segments.length()) {
					alternatives.clear();
				} else {
					break;
				}
			}

		}
		return alternatives;
	}

	private static String ordinal(int i) {
		String[] sufixes = new String[] { "th", "st", "nd", "rd", "th", "th",
				"th", "th", "th", "th" };
		switch (i % 100) {
		case 11:
		case 12:
		case 13:
			return i + "th";
		default:
			return i + sufixes[i % 10];

		}
	}

	public ArrayList<TripAlternativeRome2Rio> retrieveAlternatives(
			JSONObject jsonObj) {
		ArrayList<TripAlternativeRome2Rio> alternatives = new ArrayList<TripAlternativeRome2Rio>();

		JSONArray routes = new JSONArray();
		routes = jsonObj.getJSONArray("routes");
		// System.out.println("routes.length(): "+routes.length());
		for (int i = 0; i < routes.length(); i++) {

			// System.out.println("Soluzione " + i + ":");
			JSONObject route = (JSONObject) routes.get(i);
			// System.out.println(route);

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
			Double priceInd = 0.0;
			JSONArray prices = new JSONArray();
			if (route.has("indicativePrices")) {
				prices = route.getJSONArray("indicativePrices");
				JSONObject price = (JSONObject) prices.get(0);
				// price
				priceInd = price.getDouble("price");
			}

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
				// System.out.println("SEGMENTL " + segment);
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
					i, mean, priceInd, duration, distance, segments,
					number_changes);
			alternatives.add(alternative);

		}

		return alternatives;

	}

	public JSONObject getRome2RioResponse(String partenza, String destinazione) {

		JSONObject response = null;
		// String result =
		// callURL("http://free.rome2rio.com/api/1.4/json/Search?key=Yt1V3vTI&oName="
		// + partenza + "&dName=" + destinazione);

		// call Rome2Rio with lat and long
		// http://free.rome2rio.com/api/1.4/xml/Search?key=Yt1V3vTI&oPos=46.074779,11.121749&oKind=addressd&dPos=41.902783,12.496366&dKind=address

		GoogleAPIWrapper googleWrapper = new GoogleAPIWrapper();
		String originCoord = googleWrapper.getCoordinates(partenza);
		String destCoord = googleWrapper.getCoordinates(destinazione);
		String url = "http://free.rome2rio.com/api/1.4/json/Search?key=Yt1V3vTI&oPos="
				+ originCoord
				+ "&oKind=addressd&dPos="
				+ destCoord
				+ "&dKind=address";

		String result = callURL(url);

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
