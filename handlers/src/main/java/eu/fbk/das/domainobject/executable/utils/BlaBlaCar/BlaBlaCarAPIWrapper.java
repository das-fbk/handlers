package eu.fbk.das.domainobject.executable.utils.BlaBlaCar;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class BlaBlaCarAPIWrapper {

	public ArrayList<TripAlternativeBlaBlaCar> getBlaBlaCarAlternatives(
			String partenza, String destinazione,
			ArrayList<Long> serviceExecTime) {

		ArrayList<TripAlternativeBlaBlaCar> alternatives = new ArrayList<TripAlternativeBlaBlaCar>();

		long startParsingTime = System.nanoTime();

		String result = callURL("https://public-api.blablacar.com/api/v2/trips?key=0954ba4c89b34ab7b73a2d727e91d7ff&fn="
				+ partenza + "&tn=" + destinazione + "&cur=EUR&_format=json");

		if (serviceExecTime != null) {
			serviceExecTime.add(System.nanoTime() - startParsingTime);
		}

		if (result.equalsIgnoreCase("erroreAPI")) {
			return alternatives;
		} else {

			JSONObject jsonObj = new JSONObject(result);
			JSONArray trips = new JSONArray();
			trips = jsonObj.getJSONArray("trips");

			Integer distance = jsonObj.getInt("distance");
			Integer perfect_duration = jsonObj.getInt("duration");
			Double recommended_price = jsonObj.getDouble("recommended_price");

			for (int i = 0; i < trips.length(); i++) {

				JSONObject route = (JSONObject) trips.get(i);

				Integer seats_left = route.getInt("seats_left");

				String id = route.getString("permanent_id");

				String date = route.getString("departure_date")
						.substring(0, 10);
				String hour = route.getString("departure_date").substring(11,
						19);

				Double priceInd;
				if (route.has("price_with_commission")) {
					JSONObject price = route
							.getJSONObject("price_with_commission");
					priceInd = price.getDouble("value");
				} else {
					priceInd = 0.0;
				}

				String carmodel;
				if (route.has("car")) {
					JSONObject car = route.getJSONObject("car");
					carmodel = car.getString("make").toLowerCase() + " "
							+ car.getString("model").toLowerCase();
				} else {
					carmodel = "null";
				}

				if (seats_left > 0) {
					TripAlternativeBlaBlaCar alternative = new TripAlternativeBlaBlaCar(
							id, priceInd, seats_left, date, hour, carmodel,
							distance, perfect_duration, recommended_price);
					alternatives.add(alternative);
				}

			}
		}
		return alternatives;
	}

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
