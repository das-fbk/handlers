package eu.fbk.das.domainobject.executable.utils.GoogleAPI;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

import org.json.JSONArray;
import org.json.JSONObject;

public class GoogleAPIWrapper {

	public String getAddress(Double lat, Double longit) {
		String GoogleAPIKey = "AIzaSyBnLrMivSthmUmUipPfk5sidv7f0QvvDjg";
		String latString = lat.toString();
		String longString = longit.toString();
		String latlng = latString + ',' + longString;
		String URL = "https://maps.googleapis.com/maps/api/geocode/json?latlng="
				+ latlng + "&key=" + GoogleAPIKey;
		System.out.println(URL);
		String result = callURL(URL);
		// elaboro il JSON in uscita dalla call API
		String indirizzo = "";

		if (result.equalsIgnoreCase("erroreAPI")) {
			return indirizzo;
		} else {
			// System.out.println(result);
			JSONObject jsonObj = new JSONObject(result);
			JSONArray routes = new JSONArray();
			routes = jsonObj.getJSONArray("results");
			for (int i = 0; i < routes.length(); i++) {

				System.out.println("Soluzione " + i + ":");
				JSONObject route = (JSONObject) routes.get(i);
				System.out.println(route);
				// indirizzo corrente
				String ind = route.getString("formatted_address");
				indirizzo = ind;
				break;

			}
		}

		return indirizzo;
	}

	public String getCoordinates(String address) {
		String GoogleAPIKey = "AIzaSyBnLrMivSthmUmUipPfk5sidv7f0QvvDjg";
		String URL = "https://maps.googleapis.com/maps/api/geocode/json?address="
				+ address + "&key=" + GoogleAPIKey;
		System.out.println(URL);
		String result = callURL(URL);
		// elaboro il JSON in uscita dalla call API
		String latlong = "";

		if (result.equalsIgnoreCase("erroreAPI")) {
			return latlong;
		} else {
			// System.out.println(result);
			JSONObject jsonObj = new JSONObject(result);
			JSONArray routes = new JSONArray();
			routes = jsonObj.getJSONArray("results");
			for (int i = 0; i < routes.length(); i++) {

				JSONObject info = (JSONObject) routes.get(i);
				System.out.println(info);
				JSONObject geometry = info.getJSONObject("geometry");
				JSONObject viewport = geometry.getJSONObject("viewport");
				JSONObject coord = viewport.getJSONObject("southwest");
				System.out.println(coord);

				Double lat = coord.getDouble("lat");
				Double lng = coord.getDouble("lng");

				String latString = lat.toString();
				String lngString = lng.toString();

				latlong = latString + "," + lngString;

				break;

			}
		}

		return latlong;
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
