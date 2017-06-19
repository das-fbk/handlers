package eu.fbk.das.domainobject.executable.utils.GoogleAPI;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class GoogleAPIWrapper {

	public String getAddress(Float lat, Float longit) {
		String GoogleAPIKey = "AIzaSyBnLrMivSthmUmUipPfk5sidv7f0QvvDjg";
		String latString = lat.toString();
		String longString = longit.toString();
		String latlng = latString + ',' + longString;
		String URL = "https://maps.googleapis.com/maps/api/geocode/json?latlng="
				+ latlng + "&key=" + GoogleAPIKey;
		// System.out.println(URL);
		String result = callURL(URL);
		// JSON Elaboration
		String indirizzo = "";

		if (result.equalsIgnoreCase("erroreAPI")) {
			System.out.println("errorAPI");
		} else {
			// System.out.println(result);
			JSONObject jsonObj = new JSONObject(result);
			JSONArray routes = new JSONArray();
			routes = jsonObj.getJSONArray("results");
			for (int i = 0; i < routes.length(); i++) {

				JSONObject route = (JSONObject) routes.get(i);
				String ind = route.getString("formatted_address");
				indirizzo = ind;
				break;

			}
		}

		return indirizzo;
	}

	public ArrayList<GoogleTransitAlternative> callTransit(String source,
			String destination) {

		ArrayList<GoogleTransitAlternative> alternatives = new ArrayList<GoogleTransitAlternative>();
		String GoogleAPIKey = "AIzaSyBnLrMivSthmUmUipPfk5sidv7f0QvvDjg";

		String URL = "https://maps.googleapis.com/maps/api/directions/json?origin="
				+ source
				+ "&destination="
				+ destination
				+ "&key="
				+ GoogleAPIKey;
		String result = callURL(URL);
		JSONObject jsonObj = new JSONObject(result);
		JSONArray routes = new JSONArray();
		routes = jsonObj.getJSONArray("routes");
		for (int i = 0; i < routes.length(); i++) {
			GoogleTransitAlternative alternative = new GoogleTransitAlternative();

			JSONObject journey = (JSONObject) routes.get(i);

			// changes
			JSONArray changes = new JSONArray();
			changes = journey.getJSONArray("legs");

			alternative.setNumber_changes(changes.length());
			alternatives.add(alternative);
		}

		return alternatives;

	}

	// retrieve the information about the Province of a certain place/address.
	public String retrieveProvince(String placeID) {
		String GoogleAPIKey = "AIzaSyBnLrMivSthmUmUipPfk5sidv7f0QvvDjg";
		String URL = "https://maps.googleapis.com/maps/api/place/details/json?placeid="
				+ placeID + "&key=" + GoogleAPIKey;

		String result = callURL(URL);
		String province = "";
		// json parsing
		JSONObject jsonObj = new JSONObject(result);
		JSONObject details = new JSONObject();
		details = jsonObj.getJSONObject("result");
		JSONArray components = details.getJSONArray("address_components");
		for (int i = 0; i < components.length(); i++) {
			JSONObject jsonObj1 = new JSONObject();
			jsonObj1 = (JSONObject) components.get(i);
			// System.out.println(jsonObj1);
			JSONArray types = jsonObj1.getJSONArray("types");
			// System.out.println(types);
			String area = types.getString(0);
			if (area.equalsIgnoreCase("administrative_area_level_2")) {
				province = jsonObj1.getString("short_name");
				break;
			}

		}

		return province;
	}

	/*
	 * @SuppressWarnings("unused") public String getCoordinates(String address)
	 * { String GoogleAPIKey = "AIzaSyBnLrMivSthmUmUipPfk5sidv7f0QvvDjg"; String
	 * URL = "https://maps.googleapis.com/maps/api/geocode/json?address=" +
	 * address + "&key=" + GoogleAPIKey; // System.out.println(URL); String
	 * result = callURL(URL); // Json Parsing String latlong = "";
	 * 
	 * if (result.equalsIgnoreCase("erroreAPI")) {
	 * System.out.println("errorAPI"); } else { // System.out.println(result);
	 * JSONObject jsonObj = new JSONObject(result); JSONArray routes = new
	 * JSONArray(); routes = jsonObj.getJSONArray("results"); for (int i = 0; i
	 * < routes.length(); i++) {
	 * 
	 * JSONObject info = (JSONObject) routes.get(i); System.out.println(info);
	 * JSONObject geometry = info.getJSONObject("geometry"); JSONObject viewport
	 * = geometry.getJSONObject("viewport"); JSONObject coord =
	 * viewport.getJSONObject("southwest"); System.out.println(coord);
	 * 
	 * Double lat = coord.getDouble("lat"); Double lng = coord.getDouble("lng");
	 * 
	 * String latString = lat.toString(); String lngString = lng.toString();
	 * 
	 * latlong = latString + "," + lngString;
	 * 
	 * break;
	 * 
	 * } }
	 * 
	 * return latlong; }
	 */

	public String getPlaceID(String address) {
		String GoogleAPIKey = "AIzaSyBnLrMivSthmUmUipPfk5sidv7f0QvvDjg";
		String URL = "https://maps.googleapis.com/maps/api/geocode/json?address="
				+ address + "&key=" + GoogleAPIKey;
		// System.out.println(URL);
		String result = callURL(URL);

		String id = "";

		if (result.equalsIgnoreCase("erroreAPI")) {
			return id;
		} else {

			JSONObject jsonObj = new JSONObject(result);
			JSONArray routes = new JSONArray();
			routes = jsonObj.getJSONArray("results");
			JSONObject info = (JSONObject) routes.get(0);

			id = info.getString("place_id");

		}

		return id;
	}

	public String getGoogleAutocomplete(String city) {

		String alternatives = new String();
		String result = callURL("https://maps.googleapis.com/maps/api/place/autocomplete/json?key=AIzaSyBnLrMivSthmUmUipPfk5sidv7f0QvvDjg&input="
				+ city + "&components=country:it&language=en");

		if (result.equalsIgnoreCase("erroreAPI")) {
			return alternatives;
		} else {

			JSONObject jsonObj = new JSONObject(result);
			JSONArray predictions = new JSONArray();
			predictions = jsonObj.getJSONArray("predictions");

			for (int i = 0; i < predictions.length(); i++) {

				JSONObject numbers = (JSONObject) predictions.get(i);

				JSONArray terms = new JSONArray();
				terms = numbers.getJSONArray("terms");

				JSONObject value = (JSONObject) terms.get(1);

				if (value.getString("value").equals("Province of Trento")
						&& !alternatives.contains(numbers
								.getString("description"))) {
					alternatives = numbers.getString("description");
				}
			}
		}
		return alternatives;
	}

	public String getCoordinates(String address) {

		String completedFrom = address.replace(", ", "+");
		completedFrom = completedFrom.replace(" ", "+");

		String URL = "https://maps.googleapis.com/maps/api/geocode/json?address="
				+ completedFrom
				+ "&key=AIzaSyBnLrMivSthmUmUipPfk5sidv7f0QvvDjg";
		// System.out.println(URL);
		String result = callURL(URL);
		String latlong = "";

		if (result.equalsIgnoreCase("erroreAPI")) {
			return latlong;
		} else {
			JSONObject jsonObj = new JSONObject(result);
			JSONArray routes = new JSONArray();
			routes = jsonObj.getJSONArray("results");

			JSONObject info = (JSONObject) routes.get(0);

			JSONObject geometry = info.getJSONObject("geometry");
			JSONObject location = geometry.getJSONObject("location");

			Double lat = location.getDouble("lat");
			Double lng = location.getDouble("lng");

			String latString = lat.toString();
			String lngString = lng.toString();

			latlong = latString + "%2C" + lngString;

			return latlong;
		}
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
