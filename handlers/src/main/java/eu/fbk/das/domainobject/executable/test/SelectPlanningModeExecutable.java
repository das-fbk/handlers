package eu.fbk.das.domainobject.executable.test;

import java.util.List;

import org.w3c.dom.Element;

import se.walkercrou.places.GooglePlaces;
import se.walkercrou.places.GooglePlacesInterface;
import se.walkercrou.places.Place;
import eu.fbk.das.domainobject.executable.utils.BotTelegram.TravelAssistantBot;
import eu.fbk.das.domainobject.executable.utils.GoogleAPI.GoogleAPIWrapper;
import eu.fbk.das.process.engine.api.AbstractExecutableActivityInterface;
import eu.fbk.das.process.engine.api.DomainObjectInstance;
import eu.fbk.das.process.engine.api.ProcessEngine;
import eu.fbk.das.process.engine.api.domain.ConcreteActivity;
import eu.fbk.das.process.engine.api.domain.ProcessActivity;
import eu.fbk.das.process.engine.api.domain.ProcessDiagram;

public class SelectPlanningModeExecutable extends
		AbstractExecutableActivityInterface {

	private ProcessEngine pe;

	public SelectPlanningModeExecutable(ProcessEngine processEngine,
			TravelAssistantBot bot) {
		this.pe = processEngine;

	}

	@Override
	public void execute(ProcessDiagram proc, ProcessActivity pa) {
		ConcreteActivity currentConcrete = (ConcreteActivity) proc
				.getCurrentActivity();

		DomainObjectInstance doi = pe.getDomainObjectInstance(proc);

		// retrieve from
		Element from = doi.getStateVariableContentByName("from");
		String fromValue = from.getFirstChild().getNodeValue();

		// retrieve to
		Element to = doi.getStateVariableContentByName("to");
		String toValue = to.getFirstChild().getNodeValue();

		// add logic to select between local and global

		String proximity = SelectPlanningModeExecutable.calculateProximity(fromValue, toValue);

		// assign the value "local" or "global"
		pe.addProcVar(proc, "planner", proximity);

		currentConcrete.setExecuted(true);

		return;
	}

	public static String calculateProximity(String start, String destination) {
		String result = "";

		// using google take the place ID
		GoogleAPIWrapper wrapper = new GoogleAPIWrapper();
		// String coordinates = wrapper.getCoordinates(start);
		String placeIDStart = wrapper.getPlaceID(start);
		if (placeIDStart == "") {
			GooglePlaces client = new GooglePlaces(
					"AIzaSyBnLrMivSthmUmUipPfk5sidv7f0QvvDjg");

			List<Place> placesFrom = client.getPlacesByQuery(start,
					GooglePlacesInterface.MAXIMUM_RESULTS);
			Place firstPlace = placesFrom.get(0);
			placeIDStart = firstPlace.getPlaceId();
		}

		String placeIDDest = wrapper.getPlaceID(destination);
		if (placeIDDest == "") {
			GooglePlaces client = new GooglePlaces(
					"AIzaSyBnLrMivSthmUmUipPfk5sidv7f0QvvDjg");

			List<Place> placesDest = client.getPlacesByQuery(destination,
					GooglePlacesInterface.MAXIMUM_RESULTS);
			Place firstPlace = placesDest.get(0);
			placeIDDest = firstPlace.getPlaceId();
		}

		String provinceStart = wrapper.retrieveProvince(placeIDStart);
		String provinceDest = wrapper.retrieveProvince(placeIDDest);

		if (provinceStart == "" || provinceDest == "") {
			result = "global";
		}

		else if (provinceStart.equalsIgnoreCase(provinceDest)
				&& provinceStart.equalsIgnoreCase("TN")) {
			result = "local";

		} else {
			result = "global";
		}

		return result;
	}

	/*
	 * public static void main(String[] args) { String start = "trento"; String
	 * destination = "canazei"; GoogleAPIWrapper wrapper = new
	 * GoogleAPIWrapper(); String result = calculateProximity(start,
	 * destination); System.out.println(result);
	 * 
	 * System.out.println("End Test");
	 * 
	 * }
	 */

}