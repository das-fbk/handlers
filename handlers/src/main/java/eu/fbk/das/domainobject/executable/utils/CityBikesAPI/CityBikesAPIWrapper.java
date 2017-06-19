package eu.fbk.das.domainobject.executable.utils.CityBikesAPI;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class CityBikesAPIWrapper {

	public ArrayList<CityBikesStation> getStations(String cityID) {

		ArrayList<CityBikesStation> stations = new ArrayList<CityBikesStation>();
		String URL = "http://api.citybik.es/v2/networks/" + cityID;

		String result = callURL(URL);
		// JSON Elaboration

		if (result.equalsIgnoreCase("erroreAPI")) {
			System.out.println("errorAPI");
		} else {
			// System.out.println(result);
			JSONObject jsonObj = new JSONObject(result);
			JSONObject network = new JSONObject();
			network = jsonObj.getJSONObject("network");
			// System.out.println(network);
			JSONArray setOfStations = new JSONArray();
			setOfStations = network.getJSONArray("stations");
			for (int i = 0; i < setOfStations.length(); i++) {
				JSONObject current = (JSONObject) setOfStations.get(i);
				String name = (String) current.get("name");
				int free_bikes = current.getInt("free_bikes");
				// int empty_slots = current.getInt("empty_slots");
				CityBikesStation station = new CityBikesStation();
				station.setName(name);
				station.setFree_bikes(free_bikes);
				// station.setEmpty_slots(empty_slots);

				stations.add(station);
			}

		}
		return stations;

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
			new RuntimeException("Exception while calling URL:" + myURL, e);
			return "erroreAPI";

		}

		return sb.toString();
	}

}
