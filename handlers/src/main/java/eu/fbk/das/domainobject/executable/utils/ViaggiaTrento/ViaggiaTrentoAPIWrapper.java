package eu.fbk.das.domainobject.executable.utils.ViaggiaTrento;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONObject;

public class ViaggiaTrentoAPIWrapper {

	public JSONArray getViaggiaTrentoResponse(String coordinatesFrom,
			String coordinatesTo) {

		JSONArray response = null;
		String dateHour = new SimpleDateFormat("MM/dd/yyyy HH:mm:mm")
				.format(Calendar.getInstance().getTime());
		String date = dateHour.substring(0, 2) + "%2F"
				+ dateHour.substring(3, 5) + "%2F" + dateHour.substring(6, 10);
		String hour = dateHour.substring(11, 13) + "%3A"
				+ dateHour.substring(14, 16);

		String result = callURL("https://dev.smartcommunitylab.it/smart-planner2/trentino/rest/plan?from="
				+ coordinatesFrom
				+ "&to="
				+ coordinatesTo
				+ "&date="
				+ date
				+ "&departureTime="
				+ hour
				+ "&transportType=TRANSIT&routeType=fastest&numOfItn=5");

		if (result.equalsIgnoreCase("erroreAPI")) {
			return response;
		} else {

			response = new JSONArray(result);
		}
		return response;

	}

	public ArrayList<TravelViaggiaTrento> getViaggiaTrentoRoutes(
			String coordinatesFrom, String coordinatesTo) {

		String dateHour = new SimpleDateFormat("MM/dd/yyyy HH:mm:mm")
				.format(Calendar.getInstance().getTime());
		String date = dateHour.substring(0, 2) + "%2F"
				+ dateHour.substring(3, 5) + "%2F" + dateHour.substring(6, 10);
		String hour = dateHour.substring(11, 13) + "%3A"
				+ dateHour.substring(14, 16);
		ArrayList<TravelViaggiaTrento> alternatives = new ArrayList<TravelViaggiaTrento>();

		String result = callURL("https://dev.smartcommunitylab.it/smart-planner2/trentino/rest/plan?from="
				+ coordinatesFrom
				+ "&to="
				+ coordinatesTo
				+ "&date="
				+ date
				+ "&departureTime="
				+ hour
				+ "&transportType=TRANSIT&routeType=fastest&numOfItn=3");

		if (result.equalsIgnoreCase("erroreAPI")) {
			return alternatives;
		} else {

			JSONArray jsonArr = new JSONArray(result);

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
						String busNumber = transport
								.getString("routeShortName");
						complete = "*" + busNumber + "*\n"
								+ "          *FROM* " + nameFrom + "\n"
								+ "          *TO* " + nameTO;
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
		}
		return alternatives;
	}

	public ArrayList<TravelViaggiaTrento> retrieveAlternatives(JSONObject result) {
		ArrayList<TravelViaggiaTrento> alternatives = new ArrayList<TravelViaggiaTrento>();

		JSONArray jsonArr = new JSONArray(result);
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
